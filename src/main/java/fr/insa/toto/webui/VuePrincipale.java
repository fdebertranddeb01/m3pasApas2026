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

package fr.insa.toto.webui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import fr.insa.beuvron.utils.database.ConnectionPool;
import fr.insa.beuvron.utils.exceptions.ExceptionsUtils;
import fr.insa.toto.model.BdDTest;
import fr.insa.toto.model.GestionSchema;
import fr.insa.toto.model.Utilisateur;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

/**
 *
 * @author francois
 */
@Route(value = "")
public class VuePrincipale extends VerticalLayout{
    
    private Entete entete;
    
    public VuePrincipale() {
        this.entete = new Entete(this);
        this.add(this.entete);
        this.add(new H3("Coucou"));
        
        Button raz = new Button("RAZ BdD (debug)");
        raz.addClickListener(e -> {
           try (Connection con = ConnectionPool.getConnection())  {
               GestionSchema.razBdD(con);
               BdDTest.creeBdDTest(con);
           } catch (SQLException ex) {
                Notification.show("Problem : "+ex.getLocalizedMessage());
            }
        });
        this.add(raz);
        
        try {
            throw new Exception("titre erreur");
        } catch (Exception ex) {
            this.add(Erreurs.compoErreur(ex.getLocalizedMessage(), 
                    ExceptionsUtils.messageEtPremiersAppelsDansPackage(ex, "fr.insa", 4)));
        }
    }
    
    public void updateView() {
        this.entete.updateView();
    }
}
