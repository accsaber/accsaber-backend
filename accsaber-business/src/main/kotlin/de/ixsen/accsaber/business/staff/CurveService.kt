package de.ixsen.accsaber.business.staff

import de.ixsen.accsaber.database.repositories.model.PlayerDataRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression
import org.springframework.stereotype.Service
import java.sql.SQLException
import javax.persistence.EntityManager
import javax.transaction.Transactional

@Service
@ConditionalOnExpression("\${accsaber.enable-curve-query}")
class CurveService @Autowired constructor(
    private val entityManager: EntityManager,
    private val playerDataRepository: PlayerDataRepository,
) {

    @Transactional(rollbackOn = [SQLException::class])
    fun saveNewCurve(curve: String) {
        val functionCreationQuery = getFunctionCreationQuery(curve)
        this.entityManager.joinTransaction()
        this.entityManager.createNativeQuery("DROP FUNCTION IF EXISTS calc_ap").executeUpdate()
        this.entityManager.createNativeQuery(functionCreationQuery).executeUpdate()
        this.playerDataRepository.recalculateAllAp()
    }

    private fun getFunctionCreationQuery(curve: String) = """
CREATE FUNCTION calc_ap(accuracy double, complexity double, category_id mediumtext) returns double
BEGIN ${curve}
END;"""

    @Transactional
    fun resetCurve() {
        this.saveNewCurve(
            """
SELECT ap_curve_a, ap_curve_b, ap_curve_c, ap_curve_d, ap_curve_e
INTO @a, @b,@c,@d, @e
FROM category c
WHERE c.id = category_id;
RETURN (pow(@a, (pow((accuracy + (complexity / @e)) / @b, @c))) - 1.0) * @d;"""
        )
    }
}