package com.gjie.kgboot.api.http.strategy.analysisresp;

import java.util.HashMap;
import java.util.Map;

public class RespAnalysisFactory {
    private static Map<String, AbstractRespAnalysis> data = new HashMap<>();

    public static void registerRespAnalysis(AbstractRespAnalysis respAnalysis) {
        data.put(respAnalysis.analysisCode(), respAnalysis);
    }

    public static AbstractRespAnalysis getRespAnalysis(String code) {
        return data.get(code);
    }
}
