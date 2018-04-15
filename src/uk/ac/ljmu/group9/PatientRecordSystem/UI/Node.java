package uk.ac.ljmu.group9.PatientRecordSystem.UI;

import java.util.List;

// The application menu works as a folder structure. Some nodes are "folders", while others are "files" bound to an action.
public class Node
{
    // A UI node can either have an action or some child nodes associated with it.
    // When it has an action, that action is executed on selecting the node. Afterwards, the application proceeds to NextNode.
    // When it has child objects, the user is prompted to choose which child node they want to proceed to.

    public Node ParentNode;
    public List<Node> ChildNodes;
    public String Name;

    private IActionController actionController;
    private String actionName;
    private Node nextNode;

    // For action nodes, returns the node that the application should proceed to after executing the action.
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

    // constructor for nodes that are mapped to actions, but proceed to a specific node after executing the action
    // instead of returning to the parent node.
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

    // constructor for nodes with multiple child nodes
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
