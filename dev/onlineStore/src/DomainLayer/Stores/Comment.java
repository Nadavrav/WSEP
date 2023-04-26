package DomainLayer.Stores;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Comment extends Rating {


    private Map<Integer, LinkedList<String>> CommentMap;
    public  Comment(){
        super(0);
        CommentMap= new ConcurrentHashMap<>();
    }
    public void AddComment(int registerId,String comment){
        if(comment == null)
        {
            throw new IllegalArgumentException("Comment can not be null");
        }
        if (CommentMap.get(registerId)==null) {
            LinkedList list = new LinkedList<>();
            list.add(comment);
            CommentMap.put(registerId,list);
        }else {
            CommentMap.get(registerId).add(comment);
            CommentMap.put(registerId, CommentMap.get(registerId));
        }
    }
    public  LinkedList<String>  GetComments(int registerId){
        return CommentMap.get(registerId);
    }

    }
