package tag;

import com.dr.archive.tag.entity.TagType;
import com.dr.archive.tag.service.TagTypeService;
import com.dr.archive.util.UUIDUtils;
import org.jetbrains.annotations.TestOnly;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author: qiuyf
 * @date: 2022/3/21 14:21
 */
public class TestImp {
    @Autowired
    TagTypeService tagTypeService;

    @TestOnly
    public void testImp(){
        String dirPath = "F:\\标签关键字\\标签关键字";
        File dir = new File(dirPath);
        dirdataImp(dir ,"");
    }

    private void dirdataImp(File dir,String parentId){
        for (File file : dir.listFiles()) {
            String fName = file.getName();
            if(file.isDirectory()){
                TagType tagType = new TagType();
                String id = UUIDUtils.getUUID();
                tagType.setTypeName(fName);
                tagType.setLeaf(0);
                tagType.setId(id);
                tagType.setParentId(parentId);
                tagTypeService.insert(tagType);
                dirdataImp(file,id);
            }else if(file.getName().endsWith(".txt")){
                try{
                    BufferedReader br = new BufferedReader(new FileReader(file));
                    while (!"".equals(br.readLine()) && br.readLine() != null){
                        TagType tagType = new TagType();
                        String id = UUIDUtils.getUUID();
                        tagType.setTypeName(br.readLine());
                        tagType.setLeaf(1);
                        tagType.setId(id);
                        tagType.setParentId(parentId);
                        tagTypeService.insert(tagType);
                    }
                }catch (IOException ignored){

                }
            }
        }
    }
}
