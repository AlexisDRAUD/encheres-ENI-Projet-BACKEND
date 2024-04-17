package fr.eni.ecole.enchereseniprojetbackend.bll.jpa;

import fr.eni.ecole.enchereseniprojetbackend.bll.CategorieService;
import fr.eni.ecole.enchereseniprojetbackend.bo.Categorie;
import fr.eni.ecole.enchereseniprojetbackend.dal.CategorieRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
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
    public Categorie consulterCategorieParId(long id) throws ResponseStatusException {
        Categorie existingCategorie = categorieRepository.findById(id).orElse(null);
        if (existingCategorie != null) {
            return existingCategorie;
        }else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "cette catégorie n'existe pas");
        }
    }

    @Override
    public void creerCategorie(Categorie categorie) throws ResponseStatusException{
        List<Categorie> categories = categorieRepository.findAll();
        List<String> libelles = new ArrayList<>();
        categories.forEach(cat -> libelles.add(cat.getLibelle().toLowerCase()));
        if(!libelles.contains(categorie.getLibelle().toLowerCase())){
            categorieRepository.save(categorie);
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "catégorie existante");
        }
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
    public void supprimerCategorie(long id) throws ResponseStatusException{
        Categorie existingCategorie = categorieRepository.findById(id).orElse(null);
        if (existingCategorie != null) {
            categorieRepository.deleteById(id);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "cette catégorie n'existe pas");
        }
    }
}
