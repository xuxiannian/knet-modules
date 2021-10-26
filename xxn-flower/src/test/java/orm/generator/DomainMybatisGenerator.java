package orm.generator;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.exception.InvalidConfigurationException;
import org.mybatis.generator.internal.DefaultShellCallback;

/**
 *
 * @author 徐先念
 */
public class DomainMybatisGenerator {
	
    @Test
    public void testGenerateMybatis() throws Exception {
        List<String> warnings = new ArrayList<String>();
        ConfigurationParser cp = new ConfigurationParser(warnings);
        Configuration config = cp.parseConfiguration(this.getClass().getClassLoader().getResourceAsStream("orm-generator.xml"));
            
        DefaultShellCallback shellCallback = new DefaultShellCallback(true);

        
        boolean gotException = false;
        try {
            MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, shellCallback, warnings);
            myBatisGenerator.generate(null);
        } catch (InvalidConfigurationException e) {
            assertEquals(1, e.getErrors().size());
            gotException = true;
        }
        
        
     

        if (!gotException) {
//            fail("Should throw InvalidConfigurationException");
        	System.out.println("success!");
        }
    }
	
}
