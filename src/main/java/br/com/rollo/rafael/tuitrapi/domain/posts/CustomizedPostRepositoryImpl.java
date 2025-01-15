package br.com.rollo.rafael.tuitrapi.domain.posts;

import br.com.rollo.rafael.tuitrapi.domain.users.User;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

public class CustomizedPostRepositoryImpl implements CustomizedPostRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Post> findAllPostsOf(User user) {
        var whereClause = "WHERE p.replyingTo = null AND p.author.username = :username";
        return selectWithMultipleFetch(user, whereClause);
    }

    @Override
    public List<Post> findAllPostsOfFollowedAccountsBy(User user) {
        var whereClause = "WHERE p.replyingTo = null AND (p.author.username = :username OR p.author.username IN " +
                "(select following.username from User u join u.following following where u.username = :username))";
        return selectWithMultipleFetch(user, whereClause);
    }

    /**
     * Fetch posts by user with base planned query to avoid N+1 queries and multiple bag fetching problems.
     * See the Vlad Mihalceas' answer on https://stackoverflow.com/a/51055523 to better understanding the implementation strategy
     * @param user the user who triggers the action to load its posts, the followed people's posts, or any other condition.
     * @param jpqlWhereClause the JPQL string that has a where clause condition to apply
     * @return the list of posts fetched
     */
    private List<Post> selectWithMultipleFetch(User user, String jpqlWhereClause) {
        var posts = entityManager.createQuery("select distinct p from Post p " +
                "left join fetch p.lovers " +
                jpqlWhereClause, Post.class)
                .setParameter("username", user.getUsername())
                .getResultList();

        posts = entityManager.createQuery("select distinct p from Post p " +
                "left join fetch p.replies rpl " +
                "where p in :posts ", Post.class)
                .setParameter("posts", posts)
                .getResultList();

        posts = entityManager.createQuery("select distinct p from Post p " +
                "left join fetch p.reposts rps " +
                "where p in :posts " +
                "order by p.createdAt desc", Post.class)
                .setParameter("posts", posts)
                .getResultList();

        return posts;
    }
}
