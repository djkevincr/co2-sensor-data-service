/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.iot.co2sensor.repo;

import org.iot.co2sensor.model.Measurement;
import org.iot.co2sensor.model.Metrics;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;

/**
 * Measurement Repository.
 *
 * @author Kevin Ratnasekera
 */
@Repository
public interface MeasurementRepository extends CrudRepository<Measurement, Long> {

  @Query(value = "SELECT * FROM measurements WHERE sensor_id =?1 AND server_time >= ?2 ORDER BY server_time DESC LIMIT 3 OFFSET 0",
          nativeQuery = true)
  List<Measurement> findLastThreeMeasurements(String sensorId, OffsetDateTime time);

  @Query(value = "SELECT new org.iot.co2sensor.model.Metrics(max(m.co2), cast(avg(m.co2) as double)) FROM Measurement m WHERE m.sensorId = :sensorId and  m.serverTime >= :from")
  Metrics getSensorMetrics(String sensorId, OffsetDateTime from);

}
