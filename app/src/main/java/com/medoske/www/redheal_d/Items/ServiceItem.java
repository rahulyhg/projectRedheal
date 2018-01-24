package com.medoske.www.redheal_d.Items;

/**
 * Created by USER on 30.3.17.
 */
public class ServiceItem {

    String service;
    String serviceId;
    String serviceRedhealId;


    public ServiceItem(String service, String serviceId, String serviceRedhealId) {
        this.service = service;
        this.serviceId = serviceId;
        this.serviceRedhealId = serviceRedhealId;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceRedhealId() {
        return serviceRedhealId;
    }

    public void setServiceRedhealId(String serviceRedhealId) {
        this.serviceRedhealId = serviceRedhealId;
    }
}
