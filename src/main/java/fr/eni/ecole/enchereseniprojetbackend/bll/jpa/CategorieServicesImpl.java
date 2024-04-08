package fr.eni.ecole.enchereseniprojetbackend.bll.jpa;

import fr.eni.ecole.enchereseniprojetbackend.bll.CategorieService;
import fr.eni.ecole.enchereseniprojetbackend.bo.Categorie;
import fr.eni.ecole.enchereseniprojetbackend.dal.CategorieRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class CategorieServicesImpl implements CategorieService {

    final CategorieRepository categorieRepository;

    public CategorieServicesImpl(CategorieRepository categorieRepository) {
        this.categorieRepository = categorieRepository;
    }

    @Override
    public List<Categorie> consulterCategorie() {
        return categorieRepository.findAll();
    }

    @Override
    public Categorie consulterCategorieParId(long id) {
        return categorieRepository.findByid(id);
    }

    @Override
    public void creerCategorie(Categorie Categorie) {
        categorieRepository.save(Categorie);
    }

    @Override
    public void editCategorie(Categorie Categorie) {
        Categorie existingCategorie = categorieRepository.findById(Categorie.getId()).orElse(null);

        if (existingCategorie != null) {
            existingCategorie.setLibelle(Categorie.getLibelle());

            categorieRepository.save(existingCategorie);
        }

    }

    @Override
    public void supprimerCategorie(long id) {
        categorieRepository.deleteAllById(Collections.singleton(id));
    }
}
