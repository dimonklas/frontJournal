package autotest.utils;

import autotest.database.MongoDB;
import java.util.LinkedHashMap;
import java.util.Map;

public class DataBaseUtils {
    private Utils utils = new Utils();

    public Map<String, String> getClaimsDataFromDataBase(String ref) throws Exception {
        Map<String, String> updatedData = new LinkedHashMap<String, String>();

        String claimInfo = MongoDB.INSTANCE.getClaimInfoByRef(ref).get(0);

        String dateFromDB = utils.getRegex("\"dm\"[\\s\\S]*\"\\$date\"\\s*:\\s*([\\d]*)", claimInfo);
        String date = utils.changeDateFormatFromTimestamp(dateFromDB, "dd MMM yyyy HH:mm");

        updatedData.put("date", Utils.changeDate(date, "dd MMM yyyy HH:mm", 11, 0));

        String operation = utils.getRegex("\"idp\"\\s*:\\s*\"([\\S]+?)\"", claimInfo);
        String idSystem =  utils.getRegex("\"ids\"\\s*:\\s*\"([\\S]+?)\"", claimInfo);
        String operationInfo = MongoDB.INSTANCE.getClaimInfo(idSystem.toUpperCase(), operation.toUpperCase()).get(0);
        String operationName = utils.getRegex("\"RU\"\\s*:\\s*\"([\\S\\s]+?)\"", operationInfo);

        updatedData.put("name", operationName);
        updatedData.put("fio", utils.getRegex("\"fio\"\\s*:\\s*\"([\\S\\s]+?)\"", claimInfo));
        updatedData.put("inn", utils.getRegex("\"inn\"\\s*:\\s*\"([\\S\\s]+?)\"", claimInfo));
        updatedData.put("ldap", utils.getRegex("\"ld\"\\s*:\\s*\"([\\S]+?)\"", claimInfo));

        String stateId = utils.getRegex("\"st\"\\s*:\\s*\"([\\S]+?)\"", claimInfo);
        String stateInfo = MongoDB.INSTANCE.getClaimStatusInfo(stateId).get(0);

        updatedData.put("state", utils.getRegex("\"RU\"\\s*:\\s*\"([\\S]+?)\"", stateInfo));
        updatedData.put("details", utils.getRegex(",\\s*\"dt\"\\s*:\\s*\"<[\\S\\s]*>([\\S\\s]+?)<[\\S\\s]*>\"", claimInfo));

        return updatedData;
    }
}
