package DAL.Entities;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

public class EmploymentEntityPK implements Serializable {
    @Column(name = "employee")
    @Id
    private String employee;
    @Column(name = "storeId")
    @Id
    private int storeId;

    public EmploymentEntityPK(String employee, int storeId){
        this.employee = employee;
        this.storeId = storeId;

    }
    public EmploymentEntityPK(){

    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EmploymentEntityPK that = (EmploymentEntityPK) o;

        if (storeId != that.storeId) return false;
        if (employee != null ? !employee.equals(that.employee) : that.employee != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = employee != null ? employee.hashCode() : 0;
        result = 31 * result + storeId;
        return result;
    }
}
