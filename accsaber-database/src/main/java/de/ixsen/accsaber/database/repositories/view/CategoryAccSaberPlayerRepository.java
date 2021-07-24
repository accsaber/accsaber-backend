package de.ixsen.accsaber.database.repositories.view;

import de.ixsen.accsaber.database.repositories.ReadOnlyRepository;
import de.ixsen.accsaber.database.views.AccSaberPlayer;
import de.ixsen.accsaber.database.views.CategoryAccSaberPlayer;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryAccSaberPlayerRepository extends ReadOnlyRepository<CategoryAccSaberPlayer, String, AccSaberPlayer> {
//public interface CategoryAccSaberPlayerRepository extends JpaRepository<CategoryAccSaberPlayer, String> {

//    List<CategoryAccSaberPlayer> findAllByCategory
}
