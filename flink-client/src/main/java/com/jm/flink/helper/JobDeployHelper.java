/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jm.flink.helper;


import com.jm.flink.base.bean.JobOptions;
import com.jm.flink.base.bean.JobParams;
import com.jm.flink.base.utils.GsonUtil;
import com.jm.flink.base.utils.JsonUtil;

/**
 * 任务环境提交类 备注: 扩展系统初始相关参数使用
 *
 * @author tasher
 * @created 2022/4/8
 */
public class JobDeployHelper {

    private JobOptions jobOptions;

    /**
     * 解析后的任务参数
     */
    private JobParams jobParams;

    private void initJobOptions() {
        if (null != jobOptions) {
            jobParams = JsonUtil.toObject(jobOptions.getJobParams(), JobParams.class);
            if (null == jobParams) {
                jobParams = new JobParams();
            }
        }
    }

    public JobDeployHelper(JobOptions jobOptions) {
        this.jobOptions = jobOptions;
        initJobOptions();
    }

    public JobOptions getJobOptions() {
        return jobOptions;
    }

    public void setJobOptions(JobOptions jobOptions) {
        this.jobOptions = jobOptions;
    }

    public JobParams getJobParams() {
        return jobParams;
    }

    public void setJobParams(JobParams jobParams) {
        this.jobParams = jobParams;
    }
}
