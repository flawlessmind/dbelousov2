package ru.mirea.pkmn.belousovdo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.mirea.pkmn.belousovdo.repository.PkmnRepository;
import ru.mirea.pkmn.belousovdo.entity.CardEntity;
import ru.mirea.pkmn.belousovdo.repository.PkmnRepositoryImpl;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Logger;

@WebServlet(urlPatterns = "/cards/*", loadOnStartup = 1)
public class CardServlet extends HttpServlet {

    PkmnRepository pkmnRepository;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        // Retrieve the EntityManager from the servlet context
        EntityManager em = (EntityManager) config.getServletContext().getAttribute("entityManager");
        Logger logger = Logger.getLogger(CardServlet.class.getName());

        if (em != null) {
            pkmnRepository = new PkmnRepositoryImpl(em, logger);
            logger.info("PkmnRepository initialized successfully");
        } else {
            logger.severe("EntityManager is null in CardServlet");
            throw new ServletException("Failed to initialize CardServlet: EntityManager is null");
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String cardName = req.getParameter("name");
        CardEntity card = pkmnRepository.getCard(cardName);

        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();

        ObjectMapper objectMapper = new ObjectMapper();

        if (card != null) {
            out.write(objectMapper.writeValueAsString(card));
        } else {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            out.write("{\"error\": \"Card not found\"}");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        PrintWriter out = resp.getWriter();

        resp.setContentType("application/json");

        CardEntity card = objectMapper.readValue(req.getInputStream(), CardEntity.class);
        pkmnRepository.saveCard(card);

        CardEntity savedCard = pkmnRepository.getCard(card.getName());

        if (savedCard != null) {
            out.write(objectMapper.writeValueAsString(card));
            resp.setStatus(HttpServletResponse.SC_CREATED);
        } else {
            resp.setStatus(500);
        }
    }
}
