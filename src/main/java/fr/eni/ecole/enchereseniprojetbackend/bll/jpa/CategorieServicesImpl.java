package fr.eni.ecole.enchereseniprojetbackend.bll.jpa;

import fr.eni.ecole.enchereseniprojetbackend.bll.CategorieService;
import fr.eni.ecole.enchereseniprojetbackend.bo.Categorie;
import fr.eni.ecole.enchereseniprojetbackend.dal.CategorieRepository;
import org.springframework.stereotype.Service;

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
    public Categorie consulterCategorieParId(long id) {
        return categorieRepository.findByid(id);
    }

    @Override
    public String creerCategorie(Categorie categorie) {
        String result = "OK";
        List<Categorie> categories = categorieRepository.findAll();
        List<String> libelles = new ArrayList<>();
        categories.forEach(cat -> libelles.add(cat.getLibelle().toLowerCase()));

        //List<String> libelles = categories.stream().map(cat -> cat.getLibelle().toLowerCase());
        System.out.println("CREER" + libelles);
        if(!libelles.contains(categorie.getLibelle().toLowerCase())){
            categorieRepository.save(categorie);
            return result;
        } else {
            result = "Erreur";
            return result;
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
    public void supprimerCategorie(long id) {
        categorieRepository.deleteById(id);
    }
}
