import org.snmp4j.agent.MOAccess;
import org.snmp4j.agent.mo.*;
import org.snmp4j.smi.*;

import java.util.ArrayList;
import java.util.List;

public class MOTableBuilder {
    private MOTableSubIndex[] subIndices = new MOTableSubIndex[] {new MOTableSubIndex(SMIConstants.SYNTAX_INTEGER)};
    private MOTableIndex indexDef = new MOTableIndex(subIndices,false);
    private final List<MOColumn> cols = new ArrayList<>();
    private final List<Variable[]> lines = new ArrayList<>();
    private OID rootOID;
    private int cur_line = 0;
    private int cur_col = 0;
    private int col_type_counter = 0;

    public MOTableBuilder(OID oid) {
        rootOID = oid;
    }

    public MOTableBuilder addColumn(int syntax, MOAccess acesso) {
        col_type_counter++;
        MOColumn nova = new MOColumn(col_type_counter,syntax,acesso);
        cols.add(nova);
        return this;
    }

    public MOTableBuilder addVariable(Variable var) {
        if(lines.size() == cur_line) lines.add(new Variable[cols.size()]);
        lines.get(cur_line)[cur_col] = var;
        cur_col++;
        if(cur_col >= cols.size()) {
            cur_line++;
            cur_col = 0;
        }
        return this;
    }

    public MOTable build() {
        DefaultMOTable tabela = new DefaultMOTable(rootOID,indexDef,cols.toArray(new MOColumn[0]));
        MOMutableTableModel model = (MOMutableTableModel) tabela.getModel();
        int i = 1;
        for(Variable[] variables : lines) {
            model.addRow(new DefaultMOMutableRow2PC(new OID(String.valueOf(i)),variables));
            i++;
        }
        tabela.setVolatile(false);
        return tabela;
    }
/*
    public MOTable updateState(OID oid,long newCount, int position,MOTable tabela){
        MOMutableTableModel model = (MOMutableTableModel) tabela.getModel();
        Variable[] variables = new Variable[model.getRowCount()];
        System.out.println(variables.length);
        System.out.println(model.getRow(new OID("." + position)));
        for(int i=0;i<model.getRowCount();i++){
           variables[i]=model.getRow(new OID("." + (i + 1))).getValue(7);
        }
        variables[position]= new Counter32(newCount);
        for (Variable variable : variables) {
            System.out.println(variable);
        }
        tabela.removeRow(oid);
       // model.addRow(new DefaultMOMutableRow2PC(oid,variables));
        return tabela;
    }

    public void removeLine(OID oid,MOTable tabela) {
        MOMutableTableModel model = (MOMutableTableModel) tabela.getModel();
        model.removeRow(oid);
    }

    // MAY NEED RE-WRITING!
    public MOTable updateID(OID oid, String ID, MOTable tabela) {
        MOMutableTableModel model = (MOMutableTableModel) tabela.getModel();
        Variable[] variables = new Variable[model.getRow(oid).size()];
        for(int i = 0; i < model.getRow(oid).size();i++) {
            variables[i] = model.getRow(oid).getValue(i);
        }
        variables[4] = new OctetString(ID);
        model.removeRow(oid);
        model.addRow(new DefaultMOMutableRow2PC(oid,variables));
        return tabela;
    }


    public void addMusicEntry(MOTable tabela,long id,String name,long artistID,long albumID,long year,long genreID,long time,long listenCount) {
        MOMutableTableModel model = (MOMutableTableModel) tabela.getModel();
        int nRows = model.getRowCount()+1;
        Variable [] variables = new Variable[]{new UnsignedInteger32(id),new OctetString(name),new UnsignedInteger32(artistID), new UnsignedInteger32(albumID)
                , new UnsignedInteger32(year),new UnsignedInteger32(genreID),new UnsignedInteger32(time), new Counter32(listenCount)};
        model.addRow(new DefaultMOMutableRow2PC(new OID(""+ nRows),variables));
    }
*/

}
