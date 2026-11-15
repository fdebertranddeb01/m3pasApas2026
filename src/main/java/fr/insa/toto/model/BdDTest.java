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

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 *
 * @author francois
 */
public class BdDTest {
    
    public static Random RAND = new Random(1256);
    
    public static int NBR_USERS_ALEA = 10;
    
    public static void creeBdDTest(Connection con) throws SQLException {
        List<Utilisateur> users = List.of(
                new Utilisateur("toto", "p1", 1),
                new Utilisateur("titi", "p2", 2),
                new Utilisateur("tutu", "p3", 2)
        );
        for (var u : users) {
            u.saveInDB(con);
        }
        
        Map<Utilisateur,List<Utilisateur>> apprecie = Map.of(
                users.get(0),List.of(),
                users.get(1),List.of(users.get(1),users.get(2)),
                users.get(2),List.of(users.get(0),users.get(1))                
        );
        for (var e : apprecie.entrySet()) {
            Utilisateur.changeApprecie(con, e.getKey(), e.getValue());
        }
        
        ArrayList<Utilisateur> users2 = new ArrayList<>();
        for (int i = 0 ; i < NBR_USERS_ALEA ; i ++) {
            users2.add(new Utilisateur("u"+(i+1),"p"+(i+1),2));
        }
        for (var u2 : users2) {
            u2.saveInDB(con);
        }
        for (var u2 : users2) {
            var tous = new ArrayList<>(users2);
            Collections.shuffle(tous, RAND);
            int combien = RAND.nextInt(NBR_USERS_ALEA/2);
            Utilisateur.changeApprecie(con, u2, tous.subList(0, combien));
        }


        
        
    }
    
}
