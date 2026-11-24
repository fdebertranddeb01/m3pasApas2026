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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * classe "Miroir" de la table utilisateur. Convention : id == -1 : utilisateur
 * a été créé en mémoire mais pas sauvegardé id >= 0 : utilisateur existe dans
 * la Base de donnée
 *
 * @author francois
 */
public class Utilisateur {

    private int id;
    private String nom;
    private String pass;
    private int role;

    public Utilisateur(int id, String nom, String pass, int role) {
        this.id = id;
        this.nom = nom;
        this.pass = pass;
        this.role = role;
    }

    public Utilisateur(String nom, String pass, int role) {
        this(-1, nom, pass, role);
    }

    @Override
    public String toString() {
        return "Utilisateur{" + "id=" + id + ", nom=" + nom + ", pass=" + pass + ", role=" + role + '}';
    }

    /**
     * pour gérer l'égalité des objets en tenant compte de leur état de
     * sauvegarde dans la base de données : - si les deux objets ont un id
     * positif, on compare les id - si les deux objets ont un id négatif, on
     * compare les références (super.equals) - si un objet a un id positif et
     * l'autre un id négatif, ils ne sont pas égaux
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        final Utilisateur other = (Utilisateur) obj;
        if (this.getId() == -1) {
            if (other.getId() == -1) {
                return super.equals(obj);
            } else {
                return false;
            }
        } else {
            if (other.getId() == -1) {
                return false;
            } else {
                return this.getId() == other.getId();
            }
        }
    }

    @Override
    public int hashCode() {
        if (this.getId() == -1) {
            return super.hashCode();
        } else {
            return this.getId();
        }
    }
    
    public static Utilisateur entreConsole() {
        String nom = ConsoleFdB.entreeString("nom : ");
        String pass = ConsoleFdB.entreeString("pass : ");
        int role = -1;
        while (role != 1 && role != 2) {
            role = ConsoleFdB.entreeInt("role (1 pour admin, 2 pour user) : ");
        }
        return new Utilisateur(nom, pass, role);
    }

    public void saveInDB(Connection con) throws SQLException {
        if (this.id != -1) {
            throw new Error("utilisateur déjà dans la BdD");
        }
        try (var pst = con.prepareStatement(
                "insert into utilisateur (nom,pass,role) values (?,?,?)",
                PreparedStatement.RETURN_GENERATED_KEYS)) {
            int i = 1;
            pst.setString(i++, this.nom);
            pst.setString(i++, this.pass);
            pst.setInt(i++, this.role);
            pst.executeUpdate();
            ResultSet keys = pst.getGeneratedKeys();
            if (keys.next()) {
                this.id = keys.getInt(1);
            } else {
                throw new SQLException("aucun id généré");
            }
        }
    }

    /**
     * !! TODO : pour l'instant seulement pour un utilisateur isolé
     *
     * @param con
     * @throws SQLException
     */
    public void deleteInDB(Connection con) throws SQLException {
        if (this.id == -1) {
            throw new Error("l'utilisateur n'est pas dans la base de donnée");
        }
        try (var pst = con.prepareStatement(
                "delete from utilisateur where id = ?")) {
            int i = 1;
            pst.setInt(i++, this.id);
            pst.executeUpdate();
        }
    }

    public static Optional<Utilisateur> findByID(Connection con, int id) throws SQLException {
        try (var pst = con.prepareStatement(
                "select id,nom,pass,role from utilisateur where id = ?")) {
            int i = 1;
            pst.setInt(i, id);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                String nom = rs.getString("nom");
                String pass = rs.getString("pass");
                int role = rs.getInt("role");
                Utilisateur trouve = new Utilisateur(id, nom, pass, role);
                return Optional.of(trouve);
            } else {
                return Optional.empty();
            }
        }

    }

    public static Optional<Utilisateur> findByNomAndPass(Connection con, String nom,String pass) throws SQLException {
        try (var pst = con.prepareStatement(
                "select id,nom,pass,role from utilisateur where nom = ? and pass = ?")) {
            int i = 1;
            pst.setString(i++, nom);
            pst.setString(i++, pass);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("id");
                int role = rs.getInt("role");
                Utilisateur trouve = new Utilisateur(id, nom, pass, role);
                return Optional.of(trouve);
            } else {
                return Optional.empty();
            }
        }

    }

    public static List<Utilisateur> tousLesUtilisateurs(Connection con) throws SQLException {
        try (var pst = con.prepareStatement(
                "select id,nom,pass,role from utilisateur")) {
            ResultSet rs = pst.executeQuery();
            List<Utilisateur> res = new ArrayList<>();
            while (rs.next()) {
                int id = rs.getInt("id");
                String nom = rs.getString("nom");
                String pass = rs.getString("pass");
                int role = rs.getInt("role");
                Utilisateur trouve = new Utilisateur(id, nom, pass, role);
                res.add(trouve);
            }
            return res;
        }
    }

    public static List<Utilisateur> utilisateursAppreciesPar(Connection con, Utilisateur u1)
            throws SQLException {
        try (var pst = con.prepareStatement(
                "select id,nom,pass,role \n"
                + "from utilisateur join apprecie on apprecie.u2 = utilisateur.id\n"
                + "where apprecie.u1 = ?")) {
            pst.setInt(1, u1.getId());
            ResultSet rs = pst.executeQuery();
            List<Utilisateur> res = new ArrayList<>();
            while (rs.next()) {
                int id = rs.getInt("id");
                String nom = rs.getString("nom");
                String pass = rs.getString("pass");
                int role = rs.getInt("role");
                Utilisateur trouve = new Utilisateur(id, nom, pass, role);
                res.add(trouve);
            }
            return res;
        }
    }

    public static void changeApprecie(Connection con, Utilisateur u1,
            List<Utilisateur> apprecieParU1) throws SQLException {
        con.setAutoCommit(false);
        try {
            try (var pst = con.prepareStatement(
                    "delete from apprecie where u1 = ?")) {
                pst.setInt(1, u1.getId());
                pst.executeUpdate();
            }
            try (var pst = con.prepareStatement(
                    "insert into apprecie (u1,u2) values (?,?)")) {
                pst.setInt(1, u1.getId());
                for (var u2 : apprecieParU1) {
                    pst.setInt(2, u2.getId());
                    pst.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            con.rollback();
            throw ex;
        } finally {
            con.setAutoCommit(true);
        }

    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the nom
     */
    public String getNom() {
        return nom;
    }

    /**
     * @param nom the nom to set
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * @return the pass
     */
    public String getPass() {
        return pass;
    }

    /**
     * @param pass the pass to set
     */
    public void setPass(String pass) {
        this.pass = pass;
    }

    /**
     * @return the role
     */
    public int getRole() {
        return role;
    }

    /**
     * @param role the role to set
     */
    public void setRole(int role) {
        this.role = role;
    }

}
