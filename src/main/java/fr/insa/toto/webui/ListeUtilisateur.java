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

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.router.Route;
import fr.insa.beuvron.utils.database.ConnectionPool;
import fr.insa.beuvron.utils.exceptions.ExceptionsUtils;
import fr.insa.toto.model.Utilisateur;
import java.sql.Connection;
import java.sql.SQLException;

/**
 *
 * @author francois
 */
@Route(value = "users/liste")
public class ListeUtilisateur extends VerticalLayout {
    
    public static class RenderUser extends HorizontalLayout {
        
        private Utilisateur user;
        
        public RenderUser(Utilisateur user) {
            this.user = user;
            if (this.user.getRole() == 1) {
                this.add(VaadinIcon.THUMBS_UP.create());
            } else {
                this.add(VaadinIcon.THUMBS_DOWN.create());
            }
            this.add(new Paragraph(" "+this.user.getNom()));
        }
        
    }

    public ListeUtilisateur() {
        this.setSizeFull();
        this.add(new H3("Liste de tous les utilisateurs"));
        try (Connection con = ConnectionPool.getConnection()) {
            Grid<Utilisateur> grid = new Grid<>(Utilisateur.tousLesUtilisateurs(con));
            grid.addColumn(Utilisateur::getNom).setHeader("nom");
            grid.addColumn(Utilisateur::getRole).setHeader("roleId");
            grid.addColumn(u -> {
                if (u.getRole() == 1) {
                    return "admin";
                } else {
                    return "user";
                }
            }).setHeader("role");
            grid.addColumn(new ComponentRenderer<>(u -> new RenderUser(u))).setHeader("isAdmin?");
            this.add(grid);
        } catch (SQLException ex) {
            this.add(Erreurs.compoErreur(ex.getLocalizedMessage(),
                    ExceptionsUtils.messageEtPremiersAppelsDansPackage(ex, "fr.insa", 4)));
        }
    }

}
