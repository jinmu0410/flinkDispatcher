/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jm.flink.bean;


import com.jm.flink.base.utils.OptionRequired;

import java.io.Serializable;

/**
 * 任务提交参数
 *
 * @author jinmu
 * @created 2022/4/8
 */
public class JobOptions implements Serializable {

    private static final long serialVersionUID = 1304608594451970213L;

    @OptionRequired(required = true, description = "description a job's name")
    private String jobName;

    @OptionRequired(required = true, description = "plugin flinkdo main jar path")
    private String jobMainClassJar;

    @OptionRequired(required = true, description = "sql|stream")
    private String jobType;

    @OptionRequired(required = true, description = "DeployMode description these, default local")
    private String mode;

    @OptionRequired(required = true, description = "exec sql content or stream business")
    private String jobContent;

    @OptionRequired(required = true, description = "env params about flink,hadoop...")
    private String envParams = "{}";

    @OptionRequired(required = false, description = "custom runParams")
    private String runParams = "";

    @OptionRequired(required = false, description = "attach jobParams")
    private String jobParams = "{}";

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getJobContent() {
        return jobContent;
    }

    public void setJobContent(String jobContent) {
        this.jobContent = jobContent;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getRunParams() {
        return runParams;
    }

    public void setRunParams(String runParams) {
        this.runParams = runParams;
    }

    public String getJobParams() {
        return jobParams;
    }

    public void setJobParams(String jobParams) {
        this.jobParams = jobParams;
    }

    public String getJobMainClassJar() {
        return jobMainClassJar;
    }

    public void setJobMainClassJar(String jobMainClassJar) {
        this.jobMainClassJar = jobMainClassJar;
    }

    public String getEnvParams() {
        return envParams;
    }

    public void setEnvParams(String envParams) {
        this.envParams = envParams;
    }

}
