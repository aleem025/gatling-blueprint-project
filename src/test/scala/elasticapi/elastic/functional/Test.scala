package elasticapi.elastic.functional

import elasticapi.elastic.ElasticApiChainBuilder
import gatling.blueprint.{ConfigurableSimulation, DefaultHttpProtocolBuilder}
import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder

class Test extends ConfigurableSimulation {

  private val elasticTsv = tsv(resolveFile("elastic.tsv"))

  val scenarioBuilder: ScenarioBuilder = scenario("Elastic Smoke Test")
    .repeat(elasticTsv.readRecords.length) {
      feed(elasticTsv.queue)
        .exec(
          ElasticApiChainBuilder.create("functional")
        )
    }

  setUp(scenarioBuilder.inject(atOnceUsers(1)))
    .protocols(DefaultHttpProtocolBuilder.create())
    .assertions(global.failedRequests.count.is(0))
}
