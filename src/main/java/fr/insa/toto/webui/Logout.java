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
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import fr.insa.toto.model.Utilisateur;
import java.util.Optional;

/**
 *
 * @author francois
 */
public class Logout extends HorizontalLayout {

    private VuePrincipale main;

    private Button bLogout;

    public Logout(VuePrincipale main) {
        this.main = main;
        
        this.setWidthFull();
        this.setAlignItems(Alignment.CENTER);
        this.setJustifyContentMode(JustifyContentMode.BETWEEN);
        this.getStyle().set("border", "1px solid blue");

        this.bLogout = new Button("Logout");
        this.bLogout.addClickListener(e -> doLogout());
        Optional<Utilisateur> curUser = this.main.getCurUser();
        if (curUser.isEmpty()) {
            this.add("problème interne : pas d'utilisateur courant");
        } else {
            this.add(new H4("bonjour " + curUser.get().getNom()));
            this.add(this.bLogout);
        }
    }

    public void doLogout() {
        this.main.setCurUser(Optional.empty());
        this.main.updateView();
    }
}
