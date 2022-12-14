package br.com.rollo.rafael.tuitrapi.domain.posts;

import br.com.rollo.rafael.tuitrapi.domain.users.User;
import org.hibernate.annotations.QueryHints;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

public class CustomizedPostRepositoryImpl implements CustomizedPostRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Post> findAllPostsOf(User user) {
        String whereClause = "where p.author.id = :userId";
        return selectWithMultipleFetch(user, whereClause);
    }

    @Override
    public List<Post> findAllPostsOfFollowedAccountsBy(User user) {
        String whereClause = "where p.author.id in " +
                "(select following.id from User u join u.following following where u.id = :userId)";

        List<Post> posts = selectWithMultipleFetch(user, whereClause);

        return posts;
    }

    /**
     * Fetch posts by user with base planned query to avoid N+1 queries and multiple bag fetching problems.
     * See the Vlad Mihalceas' answer on https://stackoverflow.com/a/51055523 to better understanding the implementation strategy
     * @param user the user who triggers the action to load its posts, the followed people's posts, or any other condition.
     * @param jpqlWhereClause the JPQL string that has a where clause condition to apply
     * @return the list of posts fetched
     */
    private List<Post> selectWithMultipleFetch(User user, String jpqlWhereClause) {
        List<Post> posts = entityManager.createQuery("select distinct p from Post p " +
                "left join fetch p.lovers " +
                jpqlWhereClause, Post.class)
                .setParameter("userId", user.getId())
                .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
                .getResultList();

        posts = entityManager.createQuery("select distinct p from Post p " +
                "left join fetch p.replies rpl " +
                "where p in :posts ", Post.class)
                .setParameter("posts", posts)
                .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
                .getResultList();

        posts = entityManager.createQuery("select distinct p from Post p " +
                "left join fetch p.reposts rps " +
                "where p in :posts " +
                "order by p.createdAt desc", Post.class)
                .setParameter("posts", posts)
                .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
                .getResultList();

        return posts;
    }
}
