package cmdb;

public class VM {

    public String getResourcegroup() {
        return resourcegroup;
    }

    public void setResourcegroup(String resourcegroup) {
        this.resourcegroup = resourcegroup;
    }

    public String getVnet() {
        return vnet;
    }

    public void setVnet(String vnet) {
        this.vnet = vnet;
    }

    public String getSubnet() {
        return subnet;
    }

    public void setSubnet(String subnet) {
        this.subnet = subnet;
    }

    public String getVm() {
        return vm;
    }

    public void setVm(String vm) {
        this.vm = vm;
    }

    private String resourcegroup;
    private String vnet;
    private String subnet;
    private String vm;
}
