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
package org.iot.co2sensor.model;

import javax.persistence.Column;
import javax.persistence.Enumerated;
import javax.persistence.EnumType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import java.time.OffsetDateTime;
import java.util.Objects;

/**
 * Alert Domain Class.
 *
 * @author Kevin Ratnasekera
 */
@Table(name = "alerts", indexes = {@Index(columnList = "sensor_id"), @Index(columnList = "start_time DESC")})
@Entity
public class Alert {

  @Id
  @GeneratedValue(strategy= GenerationType.IDENTITY)
  private long id;
  @Column(name = "sensor_id")
  private String sensorId;
  @Enumerated(EnumType.STRING)
  @Column(name = "sensor_status")
  private SensorStatus sensorStatus;
  @Column(name = "start_time")
  private OffsetDateTime startTime;
  @Column(name = "end_time")
  private OffsetDateTime endTime;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getSensorId() {
    return sensorId;
  }

  public void setSensorId(String sensorId) {
    this.sensorId = sensorId;
  }

  public OffsetDateTime getStartTime() {
    return startTime;
  }

  public void setStartTime(OffsetDateTime startTime) {
    this.startTime = startTime;
  }

  public OffsetDateTime getEndTime() {
    return endTime;
  }

  public void setEndTime(OffsetDateTime endTime) {
    this.endTime = endTime;
  }

  public SensorStatus getSensorStatus() {
    return sensorStatus;
  }

  public void setSensorStatus(SensorStatus sensorStatus) {
    this.sensorStatus = sensorStatus;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Alert alert = (Alert) o;
    return id == alert.id && Objects.equals(sensorId, alert.sensorId)
            && sensorStatus == alert.sensorStatus
            && Objects.equals(startTime, alert.startTime)
            && Objects.equals(endTime, alert.endTime);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, sensorId, sensorStatus, startTime, endTime);
  }

}
