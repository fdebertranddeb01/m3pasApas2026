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

import com.vaadin.flow.server.VaadinSession;
import fr.insa.toto.model.Utilisateur;
import java.util.Optional;

/**
 *
 * @author francois
 */
public class SessionInfo {
    
    public static void login(Utilisateur u) {
        VaadinSession session = VaadinSession.getCurrent();
        if (session != null) {
            session.setAttribute(Utilisateur.class, u);
        }
    }
    
    public static void logout() {
        VaadinSession session = VaadinSession.getCurrent();
        if (session != null) {
            session.setAttribute(Utilisateur.class, null);
        }
    }
    
    public static Optional<Utilisateur> curUser() {
        VaadinSession session = VaadinSession.getCurrent();
        if (session != null) {
            Utilisateur curu = session.getAttribute(Utilisateur.class);
            if (curu == null) {
                return Optional.empty();
            } else {
                return Optional.of(curu);
            }
        } else {
            return Optional.empty();
        }      
    }
    
}
