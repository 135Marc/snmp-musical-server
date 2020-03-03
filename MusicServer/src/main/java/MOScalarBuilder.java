import org.snmp4j.agent.mo.MOAccessImpl;
import org.snmp4j.agent.mo.MOScalar;
import org.snmp4j.smi.*;

public class MOScalarBuilder {

    public static MOScalar createReadOnly(OID oid,Object value ){
        return new MOScalar(oid, MOAccessImpl.ACCESS_READ_ONLY, getVariable(value));
    }

    public static MOScalar createReadWrite(OID oid, Object value) {
        MOScalar novo = new MOScalar(oid, MOAccessImpl.ACCESS_READ_WRITE, getVariable(value));
        return novo;
    }


    private static Variable getVariable(Object value) {
        if(value instanceof String) {
            return new OctetString((String)value);
        }
        if(value instanceof Integer) {
            return new Integer32((Integer) value);
        }
        if (value instanceof Counter32) {
            return new Counter32((Long) value);
        }
        if (value instanceof UnsignedInteger32) {
            return new UnsignedInteger32(((UnsignedInteger32) value).toLong());
        }
        throw new IllegalArgumentException("Unmanaged Type: " + value.getClass());
    }

}


