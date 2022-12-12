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
        String whereClausule = "where p.author.id = :userId";
        return selectWithMultipleFetch(user, whereClausule);
    }

    @Override
    public List<Post> findAllPostsOfFollowedAccountsBy(User user) {
        String whereClausule = "where p.author.id in " +
                "(select following.id from User u join u.following following where u.id = :userId)";

        List<Post> posts = selectWithMultipleFetch(user, whereClausule);

        return posts;
    }

    private List<Post> selectWithMultipleFetch(User user, String jpqlWhereClausule) {
        List<Post> posts = entityManager.createQuery("select distinct p from Post p " +
                "left join fetch p.lovers " +
                jpqlWhereClausule, Post.class)
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
