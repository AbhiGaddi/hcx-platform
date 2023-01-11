package org.swasth.util;

import org.apache.flink.api.common.restartstrategy.RestartStrategies;
import org.apache.flink.runtime.state.StateBackend;
import org.apache.flink.runtime.state.filesystem.FsStateBackend;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.environment.CheckpointConfig;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.swasth.job.BaseJobConfig;

public class FlinkUtil {

    public static StreamExecutionEnvironment getExecutionContext(BaseJobConfig config) {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.getConfig().setUseSnapshotCompression(config.enableCompressedCheckpointing);
        env.enableCheckpointing(config.checkpointingInterval);

        /**
         * Use Blob storage as distributed state backend if enabled
         */
        if (config.enableDistributedCheckpointing.isPresent() && config.enableDistributedCheckpointing.get()) {
            StateBackend stateBackend = new FsStateBackend(config.checkpointingBaseUrl.orElse("") + "/" + config.jobName, true);
            env.setStateBackend(stateBackend);
            CheckpointConfig checkpointConfig = env.getCheckpointConfig();
            checkpointConfig.enableExternalizedCheckpoints(CheckpointConfig.ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION);
            checkpointConfig.setMinPauseBetweenCheckpoints(config.checkpointingPauseSeconds);
        }
        env.setStreamTimeCharacteristic(TimeCharacteristic.IngestionTime);
        env.setRestartStrategy(RestartStrategies.fixedDelayRestart(config.restartAttempts, config.delayBetweenAttempts));
        return env;

    }
}

