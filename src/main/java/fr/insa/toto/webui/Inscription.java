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
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import fr.insa.beuvron.utils.database.ConnectionPool;
import fr.insa.toto.model.Utilisateur;
import java.sql.Connection;
import java.sql.SQLException;

/**
 *
 * @author francois
 */
@Route(value = "utilisateur/inscription")
public class Inscription extends VerticalLayout {

    private TextField tfNom;
    private PasswordField pfPass;
    private Button bValide;

    public Inscription() {
        this.tfNom = new TextField("nom :");
        this.pfPass = new PasswordField("pass :");
        this.bValide = new Button("Valider");
        this.bValide.addClickListener(e -> doInscription());
        this.add(this.tfNom, this.pfPass, this.bValide);

    }

    public void doInscription() {
        String nom = this.tfNom.getValue();
        String pass = this.pfPass.getValue();
        Utilisateur nouveau = new Utilisateur(nom, pass, 2);
        try (Connection con = ConnectionPool.getConnection()) {
            nouveau.saveInDB(con);
        } catch (SQLException ex) {
            Notification.show(ex.getLocalizedMessage(), 5000, Notification.Position.MIDDLE);
        }
    }

}
