package de.ixsen.accsaber.database.model.players;


import org.hibernate.envers.Audited;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Audited
@Table(name = "score")
public class Score extends AbstractScore {

}
