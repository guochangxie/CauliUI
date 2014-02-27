package org.cauli.ui.selenium.page;


import com.auto.anno.At;
import com.auto.anno.Commit;
import com.auto.anno.Find;
import com.auto.anno.Title;
import com.auto.ui.browser.Auto;
import com.auto.ui.browser.BaseBrowser;
import com.auto.ui.browser.Browser;
import com.auto.ui.element.Element;
import com.auto.ui.element.IElement;
import com.auto.ui.element.TempElement;
import com.auto.ui.source.XMLParse;

import java.lang.reflect.Field;

/**
 * @author 
 */
public abstract class SourcePage extends CurrentPage {
    private String pageCommit;
    public SourcePage(String pageCommit) {
        this();
        this.pageCommit=pageCommit;
    }
    public SourcePage(){
    	if(Auto.browser()==null){
    		setBrowser(new BaseBrowser(Browser.FIREFOX));
    	}else{
    		setBrowser(Auto.browser());
    	}
    	if(this.getClass().getClassLoader().getResourceAsStream(this.getClass().getSimpleName().toLowerCase()+".xml")!=null){
        	setXmlParse(new XMLParse(this.getClass().getSimpleName().toLowerCase()+".xml"));
            addElements(getXmlParse().getTempElements());
        }
        
        if(this.getClass().isAnnotationPresent(Commit.class)){
            this.pageCommit=this.getClass().getAnnotation(Commit.class).value();
        }
        if(this.getClass().isAnnotationPresent(At.class)){
        	if(!Auto.getCurrentBrowserDriver().getCurrentUrl().contains(this.getClass().getAnnotation(At.class).value())){
        		Auto.browser().selectWindowContainsUrl(this.getClass().getAnnotation(At.class).value());
        	}         
        }else if(this.getClass().isAnnotationPresent(Title.class)){
        	if(!Auto.getCurrentBrowserDriver().getCurrentUrl().contains(this.getClass().getAnnotation(At.class).value())){
        		Auto.browser().selectWindowContainsUrl(this.getClass().getAnnotation(Title.class).value());
        	}
        }
        init();
    }

    public void setPageCommit(String pageCommit) {
        this.pageCommit = pageCommit;
    }
    
    private void init(){
    	Field[] fields = this.getClass().getDeclaredFields();
    	for(Field field:fields){
    		field.setAccessible(true);
    		if(field.isAnnotationPresent(Find.class)){
                    if(!(field.getType()== TempElement.class)){
                        throw new RuntimeException("属性类型错误,定义的属性只能够为临时元素,为TempElement类型:"+field.getName());
                    }
                Find find = field.getAnnotation(Find.class);
    			String id = find.id();
    			if(!"".equals(find.xpath())){
    				String xpath = find.xpath();
                    TempElement tempElement = new TempElement(id,xpath,null);
    				try {
						field.set(this, tempElement);
						addElement(find.id(),(TempElement) field.get(this));
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
    			}else{
    				String css = find.by();
    				try {
                        TempElement tempElement = new TempElement(id,css,null);
						field.set(this, tempElement);
						addElement(find.id(),tempElement);
    				} catch (IllegalArgumentException e) {
    					e.printStackTrace();
    				} catch (IllegalAccessException e) {
    					e.printStackTrace();
    				}
    			}
    			
    		}
    	}
    }

    public IElement $(String jquery) {
		return new Element(getBrowser(),jquery);
	}

}
