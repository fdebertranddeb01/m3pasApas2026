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

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import fr.insa.beuvron.utils.database.ConnectionPool;
import fr.insa.toto.model.Utilisateur;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

/**
 *
 * @author francois
 */
public class Login extends HorizontalLayout{
    
    private VuePrincipale main;
    
    private TextField tfNom;
    private PasswordField pfPass;
    private Button bLogin;
    private Button bInscription;
    
    public Login(VuePrincipale main) {
        this.main = main;
        
        this.setWidthFull();
        this.setAlignItems(Alignment.CENTER);
        this.setJustifyContentMode(JustifyContentMode.END);
        this.getStyle().set("border", "1px solid blue");

        this.tfNom = new TextField("nom");
        this.pfPass = new PasswordField("pass");
        this.bLogin = new Button("Login");
        this.bInscription = new Button("Inscription");
        
        this.add(this.tfNom,this.pfPass,this.bLogin,this.bInscription);
        
        this.bLogin.addClickListener((e) -> {
            doLogin();
        });
    }
    
    public void doLogin() {
        String nom = this.tfNom.getValue();
        String pass = this.pfPass.getValue();
        try (Connection con = ConnectionPool.getConnection()) {
            Optional<Utilisateur> testu = Utilisateur.findByNomEtPass(con, nom, pass);
            if (testu.isEmpty()) {
                Notification.show("Utilisateur ou mot de passe invalide");
            } else {
                SessionInfo.login(testu.get());
                this.main.updateView();
            }
        } catch (SQLException ex) {
            Notification.show("Problem : "+ex);
        }
    }
    
}
