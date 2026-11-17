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
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

/**
 *
 * @author francois
 */
public class Erreurs {
    
    public static Component compoErreur(String titre,String message) {
        VerticalLayout res = new VerticalLayout();
        res.setWidthFull();
        HorizontalLayout entete = new HorizontalLayout();
        entete.setWidthFull();
        entete.getStyle().set("border", "2px solid red");
        entete.add(VaadinIcon.WARNING.create());
        H3 htitre = new H3(titre);
        htitre.getStyle().set("color", "red");
        entete.add(htitre);
        res.add(entete);
        VerticalLayout contenu = new VerticalLayout();
        contenu.setWidthFull();
        contenu.add(new Paragraph(message));
        contenu.getStyle().set("border", "2px solid red");
        res.add(contenu);
        return res;
    }
    
}
