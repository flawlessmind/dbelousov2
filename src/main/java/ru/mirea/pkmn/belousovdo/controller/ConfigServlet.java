package ru.mirea.pkmn.belousovdo.controller;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class ConfigServlet implements ServletContextListener {

    private EntityManagerFactory entityManagerFactory;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            // Create the EntityManagerFactory and EntityManager
            entityManagerFactory = Persistence.createEntityManagerFactory("pkmnUnit");
            EntityManager entityManager = entityManagerFactory.createEntityManager();

            // Store EntityManager in the servlet context
            sce.getServletContext().setAttribute("entityManager", entityManager);
            System.out.println("EntityManager initialized successfully");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Failed to initialize EntityManager");
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        EntityManager em = (EntityManager) sce.getServletContext().getAttribute("entityManager");
        if (em != null && em.isOpen()) {
            em.close();
        }
        if (entityManagerFactory != null) {
            entityManagerFactory.close();
        }
    }
}
