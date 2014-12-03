package models;

/**
 * Created by xing on 12/2/14.
 */
public class ServiceParamter {

    private String parameterId;
    private String serviceId;
    private String parameterDataType;
    private String parameterRange;
    private String parameterEnumeration;
    private String parameterRule;
    private String parameterPurpose;

    public String getParameterId() {
        return parameterId;
    }

    public void setParameterId(String parameterId) {
        this.parameterId = parameterId;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getParameterDataType() {
        return parameterDataType;
    }

    public void setParameterDataType(String parameterDataType) {
        this.parameterDataType = parameterDataType;
    }

    public String getParameterRange() {
        return parameterRange;
    }

    public void setParameterRange(String parameterRange) {
        this.parameterRange = parameterRange;
    }

    public String getParameterEnumeration() {
        return parameterEnumeration;
    }

    public void setParameterEnumeration(String parameterEnumeration) {
        this.parameterEnumeration = parameterEnumeration;
    }

    public String getParameterRule() {
        return parameterRule;
    }

    public void setParameterRule(String parameterRule) {
        this.parameterRule = parameterRule;
    }

    public String getParameterPurpose() {
        return parameterPurpose;
    }

    public void setParameterPurpose(String parameterPurpose) {
        this.parameterPurpose = parameterPurpose;
    }
}
