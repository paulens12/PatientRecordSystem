package uk.ac.ljmu.group9.PatientRecordSystem.UI;

import java.util.List;

public class Node
{
    // a UI node can either have an action or some child nodes associated with it.
    // When it has an action, that action is executed on selecting the node. Afterwards, the application proceeds to NextNode.
    // When it has child objects, the user is prompted to choose which child node they want to proceed to.

    public Node ParentNode;
    public List<Node> ChildNodes;
    public String Name;

    private IActionController actionController;
    private String actionName;
    private Node nextNode;

    public Node GetNextNode()
    {
        if(nextNode != null)
            return nextNode;
        return ParentNode;
    }

    public boolean ExecuteAction()
    {
        if(actionController != null && actionName != null && !actionName.isEmpty())
            return actionController.ExecuteAction(actionName);
        return false;
    }

    //
    public Node(String name, IActionController controller, String action, Node nextNode){
        this(name, controller, action);
        this.nextNode = nextNode;
    }

    // constructor for nodes that are mapped directly to actions
    public Node(String name, IActionController controller, String action)
    {
        Name = name;
        ChildNodes = null;
        ParentNode = null;
        this.nextNode = null;
        this.actionController = controller;
        this.actionName = action;
    }

    //constructor for nodes with children
    public Node(String name, List<Node> children)
    {
        Name = name;
        ParentNode = null;
        ChildNodes = children;
        this.nextNode = null;
        for(Node n : children)
        {
            n.ParentNode = this;
            if(n.nextNode != null)
                n.nextNode.ParentNode = this;
        }
    }

}
