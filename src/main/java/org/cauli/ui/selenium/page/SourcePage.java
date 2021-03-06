package org.cauli.ui.selenium.page;



import org.cauli.ui.annotation.At;
import org.cauli.ui.annotation.Commit;
import org.cauli.ui.annotation.Find;
import org.cauli.ui.annotation.Title;
import org.cauli.ui.selenium.browser.Auto;
import org.cauli.ui.selenium.browser.Browser;
import org.cauli.ui.selenium.browser.Engine;
import org.cauli.ui.selenium.browser.IBrowser;
import org.cauli.ui.selenium.element.TempElement;

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
    		setBrowser(new Browser(Engine.FIREFOX));
    	}else{
    		setBrowser(Auto.browser());
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
        		Auto.browser().selectWindowByTitle(this.getClass().getAnnotation(Title.class).value());
        	}
        }
        init();
    }

    public SourcePage(IBrowser browser){
        setBrowser(browser);

        if(this.getClass().isAnnotationPresent(Commit.class)){
            this.pageCommit=this.getClass().getAnnotation(Commit.class).value();
        }
        if(this.getClass().isAnnotationPresent(At.class)){
            if(!Auto.getCurrentBrowserDriver().getCurrentUrl().contains(this.getClass().getAnnotation(At.class).value())){
                Auto.browser().selectWindowContainsUrl(this.getClass().getAnnotation(At.class).value());
            }
        }else if(this.getClass().isAnnotationPresent(Title.class)){
            if(!Auto.getCurrentBrowserDriver().getCurrentUrl().contains(this.getClass().getAnnotation(At.class).value())){
                Auto.browser().selectWindowByTitle(this.getClass().getAnnotation(Title.class).value());
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
    			if(!"".equals(find.value())){
    				String value = find.value();
                    TempElement tempElement = new TempElement(id,value);
    				try {
						field.set(this, tempElement);
						addElement(find.id(),(TempElement) field.get(this));
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
    			}
    			
    		}
    	}
    }

}
