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
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import java.time.OffsetDateTime;
import java.util.Objects;

/**
 * Measurement Domain Class.
 *
 * @author Kevin Ratnasekera
 */
@Table(name = "measurements", indexes = {@Index(columnList = "sensor_id"), @Index(columnList = "server_time DESC")})
@Entity
public class Measurement {

  @Id
  @GeneratedValue(strategy= GenerationType.IDENTITY)
  private long id;
  @Column(name = "sensor_id")
  private String sensorId;
  @Column(name = "co2_level")
  private Long co2;
  @Column(name = "server_time")
  private OffsetDateTime serverTime;
  @Column(name = "client_time")
  private OffsetDateTime clientTime;

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

  public Long getCo2() {
    return co2;
  }

  public void setCo2(Long co2) {
    this.co2 = co2;
  }

  public OffsetDateTime getServerTime() {
    return serverTime;
  }

  public void setServerTime(OffsetDateTime serverTime) {
    this.serverTime = serverTime;
  }

  public OffsetDateTime getClientTime() {
    return clientTime;
  }

  public void setClientTime(OffsetDateTime clientTime) {
    this.clientTime = clientTime;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Measurement that = (Measurement) o;
    return id == that.id && Objects.equals(sensorId, that.sensorId)
            && Objects.equals(co2, that.co2)
            && Objects.equals(serverTime, that.serverTime)
            && Objects.equals(clientTime, that.clientTime);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, sensorId, co2, serverTime, clientTime);
  }
}
