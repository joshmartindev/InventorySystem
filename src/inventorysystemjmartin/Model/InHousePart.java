/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inventorysystemjmartin.Model;

/**
 *
 * @author joshp
 */
public class InHousePart extends Part {
    private int machineId;

    public InHousePart(int id, String name, double price, int stock, int min, int max, int machineid) {
        super(id, name, price, stock, min, max);
        machineId = machineid;
    }

    public void setMachineId(int machineid) {
        machineId = machineid;
    }

    public int getMachineId() {
        return machineId;
    }
    
}
