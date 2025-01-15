package br.com.rollo.rafael.tuitrapi.infrastructure.security;

import io.jsonwebtoken.lang.Assert;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.acls.jdbc.JdbcMutableAclService;
import org.springframework.security.acls.jdbc.LookupStrategy;
import org.springframework.security.acls.model.AclCache;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;

public class MySqlJdbcMutableAclService extends JdbcMutableAclService {

    private static final String DEFAULT_INSERT_INTO_ACL_CLASS = "insert into acl_class (class) values (?)";
    private static final String DEFAULT_INSERT_INTO_ACL_CLASS_WITH_ID = "insert into acl_class (class, class_id_type) values (?, ?)";

    private final String insertSid = "insert into acl_sid (principal, sid) values (?, ?)";

    public MySqlJdbcMutableAclService(DataSource dataSource, LookupStrategy lookupStrategy, AclCache aclCache) {
        super(dataSource, lookupStrategy, aclCache);
    }

    @Override
    protected Long createOrRetrieveClassPrimaryKey(String type, boolean allowCreate, Class idType) {
        var selectClassPrimaryKey = "select id from acl_class where class=?";
        List<Long> classIds = this.jdbcOperations.queryForList(selectClassPrimaryKey, Long.class, type);

        if (!classIds.isEmpty()) {
            return classIds.get(0);
        }

        if (allowCreate) {
            String sql;
            String[] params;
            if (!isAclClassIdSupported()) {
                sql = DEFAULT_INSERT_INTO_ACL_CLASS;
                params = new String[]{type};
            }
            else {
                sql = DEFAULT_INSERT_INTO_ACL_CLASS_WITH_ID;
                params = new String[]{type, idType.getCanonicalName()};
            }

            var keyHolder = new GeneratedKeyHolder();
            this.jdbcOperations.update(connection -> {
                        var preparedStatement =
                                connection.prepareStatement(sql,
                                        Statement.RETURN_GENERATED_KEYS);
                        preparedStatement.setString(1, params[0]);
                        if (params.length > 1) {
                            preparedStatement.setString(2, params[1]);
                        }
                        return preparedStatement;
                    }, keyHolder
            );
            Assert.isTrue(TransactionSynchronizationManager.isSynchronizationActive(), "Transaction must be running");
            return (Long) Objects.requireNonNull(keyHolder.getKeys()).get("id");
        }

        return null;
    }

    protected Long createOrRetrieveSidPrimaryKey(String sidName, boolean sidIsPrincipal, boolean allowCreate) {
        var selectSidPrimaryKey = "select id from acl_sid where principal=? and sid=?";
        List<Long> sidIds = this.jdbcOperations.queryForList(selectSidPrimaryKey, Long.class,
                sidIsPrincipal, sidName);
        if (!sidIds.isEmpty()) {
            return sidIds.get(0);
        }
        if (allowCreate) {
            var keyHolder = new GeneratedKeyHolder();
            this.jdbcOperations.update(connection -> {
                        var preparedStatement =
                                connection.prepareStatement(insertSid,
                                        Statement.RETURN_GENERATED_KEYS);
                        preparedStatement.setBoolean(1, sidIsPrincipal);
                        preparedStatement.setString(2, sidName);
                        return preparedStatement;
                    }, keyHolder
            );

            Assert.isTrue(TransactionSynchronizationManager.isSynchronizationActive(), "Transaction must be running");
            return (Long) Objects.requireNonNull(keyHolder.getKeys()).get("id");
        }
        return null;
    }
}
