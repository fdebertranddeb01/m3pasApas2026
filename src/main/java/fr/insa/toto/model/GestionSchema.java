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

import fr.insa.beuvron.utils.database.ConnectionSimpleSGBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author francois
 */
public class GestionSchema {

    public static void creationSchema(Connection con) throws SQLException {
        con.setAutoCommit(false);
        try {
            try (PreparedStatement pst = con.prepareStatement(
                    "CREATE TABLE utilisateur (\n"
                    + ConnectionSimpleSGBD.SGBD.declarePrimaryKeyGenerated("id")
                    + "  nom VARCHAR(30) NOT NULL UNIQUE,\n"
                    + "  pass VARCHAR(30) NOT NULL,\n"
                    // role : 1 pour administrateur ; 2 utilisateur "normal"
                    + "  role INT\n"
                    + ")"
            )) {
                pst.executeUpdate();
            }

            // l'utilisateur u1 apprécie l'utilisateur u2
            try (PreparedStatement pst = con.prepareStatement(
                    "CREATE TABLE apprecie (\n"
                    + "  u1 INT NOT NULL,\n"
                    + "  u2 INT NOT NULL\n"
                    + ")"
            )) {
                pst.executeUpdate();
            }

            try (PreparedStatement pst = con.prepareStatement(
                    "alter table apprecie\n"
                    + "  add constraint fk_apprecie_u1\n"
                    + "  foreign key (u1) references utilisateur(id)"
            )) {
                pst.executeUpdate();
            }

            try (PreparedStatement pst = con.prepareStatement(
                    "alter table apprecie\n"
                    + "  add constraint fk_apprecie_u2\n"
                    + "  foreign key (u2) references utilisateur(id)"
            )) {
                pst.executeUpdate();
            }

            // si j'arrive ici, toutes les création on étaient effectuée
            con.commit();
        } catch (SQLException ex) {
            con.rollback();
            throw ex;
        } finally {
            con.setAutoCommit(true);
        }
    }

    public static void suppressionSchema(Connection con) {
        try (PreparedStatement pst = con.prepareStatement(
                "drop constraint fk_apprecie_u2"
        )) {
            pst.executeUpdate();
        } catch (SQLException ex) {
            // rien à faire, je veux continuer les suppression
        }

        try (PreparedStatement pst = con.prepareStatement(
                "drop constraint fk_apprecie_u1"
        )) {
            pst.executeUpdate();
        } catch (SQLException ex) {
            // rien à faire, je veux continuer les suppression
        }

        try (PreparedStatement pst = con.prepareStatement(
                "drop table apprecie"
        )) {
            pst.executeUpdate();
        } catch (SQLException ex) {
            // rien à faire, je veux continuer les suppression
        }

        try (PreparedStatement pst = con.prepareStatement(
                "drop table utilisateur"
        )) {
            pst.executeUpdate();
        } catch (SQLException ex) {
            // rien à faire, je veux continuer les suppression
        }
    }
    
    public static void razBdD(Connection con) throws SQLException {
        suppressionSchema(con);
        creationSchema(con);
    }

}
