package de.ixsen.accsaber.database.views;

import de.ixsen.accsaber.database.model.players.PlayerData;
import org.hibernate.annotations.Immutable;

import javax.persistence.Entity;

@Immutable
@Entity
public class CategoryPlayer extends PlayerData {
}
