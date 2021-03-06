package com.pazdev.tennis.core.service;

import com.pazdev.tennis.core.HibernateUtil;
import com.pazdev.tennis.core.dto.EpreuveFullDto;
import com.pazdev.tennis.core.dto.EpreuveLightDto;
import com.pazdev.tennis.core.dto.JoueurDto;
import com.pazdev.tennis.core.dto.TournoiDto;
import com.pazdev.tennis.core.entity.Epreuve;
import com.pazdev.tennis.core.entity.Joueur;
import com.pazdev.tennis.core.repository.EpreuveRepositoryImpl;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.HashSet;

/**
 * Class TournoiService
 *
 * @author Tibo Pfeifer
 * @version 1.0
 * @date 06/11/2021
 */

public class EpreuveService {
    private final EpreuveRepositoryImpl epreuveRepository;

    public EpreuveService() {
        this.epreuveRepository = new EpreuveRepositoryImpl();
    }

    public EpreuveFullDto getEpreuveDetaillee(Long id) {
        Transaction transaction = null;
        Epreuve epreuve;
        EpreuveFullDto dto = null;
        try (Session session = HibernateUtil.getSessionFactory().getCurrentSession()) {
            transaction = session.beginTransaction();
            epreuve = epreuveRepository.getById(id);

            dto = new EpreuveFullDto();
            dto.setId(epreuve.getId());
            dto.setAnnee(epreuve.getAnnee());
            dto.setType(epreuve.getType());

            TournoiDto tournoiDto = new TournoiDto();
            tournoiDto.setId(epreuve.getId());
            tournoiDto.setNom(epreuve.getTournoi().getNom());
            tournoiDto.setCode(epreuve.getTournoi().getCode());

            dto.setTournoiDto(tournoiDto);
            dto.setParticipants(new HashSet<>());

            for(Joueur joueur : epreuve.getParticipants()){
                final JoueurDto joueurDto = new JoueurDto();
                joueurDto.setId(joueur.getId());
                joueurDto.setNom(joueur.getNom());
                joueurDto.setPrenom(joueur.getPrenom());
                joueurDto.setSexe(joueur.getSexe());
                dto.getParticipants().add(joueurDto);
            }

            transaction.commit();
        } catch (Throwable e) {
            if(transaction != null) transaction.rollback();
            e.printStackTrace();
        }
        return dto;
    }
    public EpreuveLightDto getEpreuveSansTournoi(Long id) {
        Transaction transaction = null;
        Epreuve epreuve;
        EpreuveLightDto dto = null;
        try (Session session = HibernateUtil.getSessionFactory().getCurrentSession()) {
            transaction = session.beginTransaction();
            epreuve = epreuveRepository.getById(id);

            dto = new EpreuveLightDto();
            dto.setId(epreuve.getId());
            dto.setAnnee(epreuve.getAnnee());
            dto.setType(epreuve.getType());

            transaction.commit();
        } catch (Throwable e) {
            if(transaction != null) transaction.rollback();
            e.printStackTrace();
        }
        return dto;
    }

}