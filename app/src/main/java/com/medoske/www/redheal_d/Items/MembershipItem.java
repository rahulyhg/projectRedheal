package com.medoske.www.redheal_d.Items;

/**
 * Created by USER on 10.4.17.
 */
public class MembershipItem {

    String membership;
    String membershipId;
    String membershipRedhealId;


    public MembershipItem(String membership, String membershipId, String membershipRedhealId) {
        this.membership = membership;
        this.membershipId = membershipId;
        this.membershipRedhealId = membershipRedhealId;
    }

    public String getMembership() {
        return membership;
    }

    public void setMembership(String membership) {
        this.membership = membership;
    }

    public String getMembershipId() {
        return membershipId;
    }

    public void setMembershipId(String membershipId) {
        this.membershipId = membershipId;
    }

    public String getMembershipRedhealId() {
        return membershipRedhealId;
    }

    public void setMembershipRedhealId(String membershipRedhealId) {
        this.membershipRedhealId = membershipRedhealId;
    }
}
