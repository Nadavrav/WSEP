package DAL.Entities;

import javax.persistence.*;

@Entity
@Table(name = "employment", schema = "onlinestoredb", catalog = "")
@IdClass(EmploymentEntityPK.class)
public class EmploymentEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "employee")
    private String employee;
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "storeId")
    private int storeId;
    @Basic
    @Column(name = "appointer")
    private String appointer;
    @Basic
    @Column(name = "role")
    private Object role;
    @Basic
    @Column(name = "permissions")
    private Object permissions;

    public String getEmployee() {
        return employee;
    }

    public void setEmployee(String employee) {
        this.employee = employee;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public String getAppointer() {
        return appointer;
    }

    public void setAppointer(String appointer) {
        this.appointer = appointer;
    }

    public Object getRole() {
        return role;
    }

    public void setRole(Object role) {
        this.role = role;
    }

    public Object getPermissions() {
        return permissions;
    }

    public void setPermissions(Object permissions) {
        this.permissions = permissions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EmploymentEntity that = (EmploymentEntity) o;

        if (storeId != that.storeId) return false;
        if (employee != null ? !employee.equals(that.employee) : that.employee != null) return false;
        if (appointer != null ? !appointer.equals(that.appointer) : that.appointer != null) return false;
        if (role != null ? !role.equals(that.role) : that.role != null) return false;
        if (permissions != null ? !permissions.equals(that.permissions) : that.permissions != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = employee != null ? employee.hashCode() : 0;
        result = 31 * result + storeId;
        result = 31 * result + (appointer != null ? appointer.hashCode() : 0);
        result = 31 * result + (role != null ? role.hashCode() : 0);
        result = 31 * result + (permissions != null ? permissions.hashCode() : 0);
        return result;
    }
}
