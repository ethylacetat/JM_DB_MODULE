package jm.task.core.jdbc.util;

import org.hibernate.*;

import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HibernateUtil {

    private static SessionFactory sessionFactory = null;

    private static Logger logger = Logger.getLogger(HibernateUtil.class.getName());

    private HibernateUtil() {
        // NO-OP
    }

    private static void checkedConfigured() {
        if (!Util.isHibernateConfigured()) {
            throw new RuntimeException("Hibernate is not configured");
        }
    }

    public static void setFactory(SessionFactory sessionFactory) {
        HibernateUtil.sessionFactory = sessionFactory;
    }

    public static Session getSession() {
        checkedConfigured();
        return sessionFactory.openSession();
    }

    public static void nativeExecute(String nativeSQL) {
        checkedConfigured();
        Session session = null;
        RuntimeException exception = null;
        try {
            session = getSession();
            session.createSQLQuery(nativeSQL).executeUpdate();
        } catch (RuntimeException queryEx) {
            exception = queryEx;
            throw exception;
        } finally {
            if (session != null) {
                try {
                    session.close();
                } catch (RuntimeException closeEx) {
                    logger.log(Level.WARNING, "Close exception", closeEx);
                    if (exception != null) {
                        exception.addSuppressed(exception);
                    }
                }
            }
        }
    }

    public static <T> List<T> nativeQuery(String nativeSQL, Class<T> entityClass) {
        checkedConfigured();
        Session session = null;
        RuntimeException exception = null;
        try {
            session = getSession();
            return (List<T>) session.createSQLQuery(nativeSQL).addEntity(entityClass).list();
        } catch (RuntimeException queryEx) {
            exception = queryEx;
            throw exception;
        } finally {
            if (session != null) {
                try {
                    session.close();
                } catch (RuntimeException closeEx) {
                    logger.log(Level.WARNING, "Close exception", closeEx);
                    if (exception != null) {
                        exception.addSuppressed(exception);
                    }
                }
            }
        }
    }


    public static void execute(Consumer<Session> transactionConsumer) {
        checkedConfigured();

        Session session = null;
        Transaction transaction = null;

        RuntimeException exception = null;
        try {
            session = HibernateUtil.getSession();
            transaction = session.beginTransaction();

            transactionConsumer.accept(session);

            transaction.commit();
         } catch (RuntimeException te) {
            exception = te;
            try {
                if (transaction != null) {
                    transaction.rollback();
                }
            } catch (RuntimeException rbe) {
                te.addSuppressed(rbe);
            }
            throw te;
        } finally {
            try {
                if (session != null) {
                    session.close();
                }
            } catch (RuntimeException closeExceptions) {
                logger.log(Level.WARNING, "Close exception", closeExceptions);

                if (exception != null) {
                    exception.addSuppressed(closeExceptions);
                }
            }
        }
    }
}