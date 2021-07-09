package de.ixsen.accsaber.business;

import de.ixsen.accsaber.business.exceptions.AccsaberOperationException;
import de.ixsen.accsaber.business.exceptions.ExceptionType;
import de.ixsen.accsaber.database.model.Leaderboard;
import de.ixsen.accsaber.database.repositories.LeaderboardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LeaderboardService {

    private final LeaderboardRepository leaderboardRepository;

    @Autowired
    public LeaderboardService(LeaderboardRepository leaderboardRepository) {
        this.leaderboardRepository = leaderboardRepository;
    }

    public List<Leaderboard> getAllLeaderboards() {
        return this.leaderboardRepository.findAll();
    }


    public void createNewLeaderboard(Leaderboard newLeaderboard) {
        if (this.leaderboardRepository.existsById(newLeaderboard.getName())) {
            throw new AccsaberOperationException(ExceptionType.LEADERBOARD_ALREADY_EXISTS, String.format("Leaderboard with the id {%s} already exists", newLeaderboard.getDescription()));
        }

        this.leaderboardRepository.save(newLeaderboard);
    }

    public void deleteLeaderboard(String leaderboardName) {
        if (!this.leaderboardRepository.existsById(leaderboardName)) {
            throw new AccsaberOperationException(ExceptionType.LEADERBOARD_NOT_FOUND, String.format("Leaderboard with the id {%s} was not found", leaderboardName));
        }
        this.leaderboardRepository.deleteById(leaderboardName);
    }

    public void updateDescription(String leaderboardName, String description) {
        Optional<Leaderboard> optionalLeaderboard = this.leaderboardRepository.findById(leaderboardName);
        if (optionalLeaderboard.isEmpty()) {
            throw new AccsaberOperationException(ExceptionType.LEADERBOARD_NOT_FOUND, String.format("Leaderboard with the id {%s} was not found", leaderboardName));
        }

        Leaderboard leaderboard = optionalLeaderboard.get();
        leaderboard.setDescription(description);
        this.leaderboardRepository.save(leaderboard);
    }
}
