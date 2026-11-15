/*
Copyright 2000- Francois de Bertrand de Beuvron

This file is part of CoursBeuvron.

CoursBeuvron is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

CoursBeuvron is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with CoursBeuvron.  If not, see <http://www.gnu.org/licenses/>.
 */
package fr.insa.toto.model;

import fr.insa.beuvron.utils.ConsoleFdB;
import fr.insa.beuvron.utils.database.ConnectionSimpleSGBD;
import fr.insa.beuvron.utils.exceptions.ExceptionsUtils;
import fr.insa.beuvron.utils.list.ListUtils;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author francois
 */
public class MenuPrincipalTexte {

    public static void menuBdD(Connection con) {
        int rep = -1;
        while (rep != 0) {
            int i = 1;
            System.out.println((i++) + ") raz BdD");
            System.out.println((i++) + ") ajout BdD Test");
            System.out.println((i++) + ") requète quelconque");
            System.out.println("0) pour retour");
            rep = ConsoleFdB.entreeInt("votre choix : ");
            try {
                int j = 1;
                if (rep == (j++)) {
                    GestionSchema.razBdD(con);
                    System.out.println("RAZ OK");
                } else if (rep == (j++)) {
                    BdDTest.creeBdDTest(con);
                    System.out.println("BdD créée");
                } else if (rep == (j++)) {
                    System.out.println("TODO");
                }
            } catch (Exception ex) {
                System.err.println(ExceptionsUtils.messageEtPremiersAppelsDansPackage(ex, "fr.insa", 4));
            }
        }
    }

    public static void menuUtilisateurs(Connection con) {
        int rep = -1;
        while (rep != 0) {
            int i = 1;
            System.out.println((i++) + ") lister tous les utilisateurs");
            System.out.println((i++) + ") ajouter un utilisateur");
            System.out.println((i++) + ") supprimer un utilisateur");
            System.out.println((i++) + ") lister utilisateurs appréciés");
            System.out.println((i++) + ") modifier utilisateurs appréciés");
            System.out.println("0) pour retour");
            rep = ConsoleFdB.entreeInt("votre choix : ");
            try {
                int j = 1;
                if (rep == (j++)) {
                    System.out.println("----------- tous les utilisateurs --------");
                    List<Utilisateur> tous = Utilisateur.tousLesUtilisateurs(con);
                    for (var u : tous) {
                        System.out.println(u);
                    }
                } else if (rep == (j++)) {
                    Utilisateur u = Utilisateur.entreConsole();
                    u.saveInDB(con);
                    
                } else if (rep == (j++)) {
                    List<Utilisateur> tous = Utilisateur.tousLesUtilisateurs(con);
                    Optional<Utilisateur> select = ListUtils.selectOneOrCancel(
                            "selectionnez l'utilisateur à supprimer", tous, 
                            u -> u.getId() + " " +u.getNom());
                    if (select.isPresent()) {
                        select.get().deleteInDB(con);
                    }
                    
                } else if (rep == (j++)) {
                    List<Utilisateur> tous = Utilisateur.tousLesUtilisateurs(con);
                    Optional<Utilisateur> select = ListUtils.selectOneOrCancel(
                            "selectionnez l'utilisateur dont vous voulez connaitre les appréciés", tous, 
                            u -> u.getId() + " " +u.getNom());
                    if (select.isPresent()) {
                        List<Utilisateur> app = Utilisateur.utilisateursAppreciesPar(con, select.get());
                        System.out.println(ListUtils.formatList(app, 
                                "utilisateurs appréciés par "+select.get().getNom() + " : \n",
                                "\n", "\n"));
                    }                    
                } else if (rep == (j++)) {
                    List<Utilisateur> tous = Utilisateur.tousLesUtilisateurs(con);
                    Optional<Utilisateur> select = ListUtils.selectOneOrCancel(
                            "selectionnez l'utilisateur dont vous voulez connaitre les appréciés", tous, 
                            u -> u.getId() + " " +u.getNom());
                    if (select.isPresent()) {
                        List<Utilisateur> app = Utilisateur.utilisateursAppreciesPar(con, select.get());
                        List<Utilisateur> nouveauApp = ListUtils.selectMultiple(
                                "selctionnez les utilisateurs appréciés par " + select.get().getNom(),
                                app , tous, u -> u.getId() + ":" + u.getNom());
                        Utilisateur.changeApprecie(con, select.get(), nouveauApp);
                    }
                    
                }
            } catch (Exception ex) {
                System.err.println(ExceptionsUtils.messageEtPremiersAppelsDansPackage(ex, "fr.insa", 4));
            }
        }
    }

    public static void menuPrincipal() {
        try (Connection con = ConnectionSimpleSGBD.defaultCon()) {
            int rep = -1;
            while (rep != 0) {
                int i = 1;
                System.out.println((i++) + ") menu gestion globale BdD");
                System.out.println((i++) + ") menu utilisateurs");
                System.out.println("0) pour quitter");
                rep = ConsoleFdB.entreeInt("votre choix : ");
                try {
                    int j = 1;
                    if (rep == (j++)) {
                        menuBdD(con);
                    } else if (rep == (j++)) {
                        System.out.println("TODO");
                        menuUtilisateurs(con);
                    }
                } catch (Exception ex) {
                    System.err.println(ExceptionsUtils.messageEtPremiersAppelsDansPackage(ex, "fr.insa", 4));
                }
            }
        } catch (SQLException ex) {
            System.err.println(ExceptionsUtils.messageEtPremiersAppelsDansPackage(ex, "fr.insa", 4));
        }
    }

    public static void main(String[] args) {
        menuPrincipal();
    }

}
