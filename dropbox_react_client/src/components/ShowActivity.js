import React, {Component} from 'react';

class ShowActivity extends Component{

    constructor(){
        super();
        this.state = {
            hover: false,
        };
    }

    formatActivity=((item)=>{
        console.log(item);
        if(item.activityType==="signup"){
            return(
                <div>
                    Created account on {new Date(item.activityTime).toLocaleString()}
                </div>
            )
        }
        else if(item.activityType==="login"){
            return(
                <div>
                    Last time logged in on {new Date(item.activityTime).toLocaleString()}
                </div>
            )
        }
        else if(item.activityType==="insert"){
            console.log(item);
            if(item.itemType==="f")
            {
                return(
                    <div>
                        Inserted File {item.itemName} on {new Date(item.activityTime).toLocaleString()}
                    </div>
                )
            }
            else if(item.itemType==="d") {
                return(
                    <div>
                        Inserted Directory {item.itemName} on {new Date(item.activityTime).toLocaleString()}
                    </div>
                )
            }

        }
        else if(item.activityType==="starred"){
            if(item.itemType==="f")
            {
                return(
                    <div>
                        Starred File {item.itemName} on {new Date(item.activityTime).toLocaleString()}
                    </div>
                )
            }
            else if(item.itemType==="d") {
                return(
                    <div>
                        Starred Directory {item.itemName} on {new Date(item.activityTime).toLocaleString()}
                    </div>
                )
            }
        }
        else if(item.activityType==="unstarred"){
            if(item.itemType==="f")
            {
                return(
                    <div>
                        Unstarred File {item.itemName} on {new Date(item.activityTime).toLocaleString()}
                    </div>
                )
            }
            else if(item.itemType==="d") {
                return(
                    <div>
                        Unstarred Directory {item.itemName} on {new Date(item.activityTime).toLocaleString()}
                    </div>
                )
            }
        }
        else if(item.activityType==="share"){
            if(item.itemType==="f")
            {
                return(
                    <div>
                        Shared File {item.itemName} on {new Date(item.activityTime).toLocaleString()}
                    </div>
                )
            }
            else if(item.itemType==="d") {
                return(
                    <div>
                        Shared Directory {item.itemName} on {new Date(item.activityTime).toLocaleString()}
                    </div>
                )
            }
        }
        else if(item.activityType==="unshared"){
            if(item.itemType==="f")
            {
                return(
                    <div>
                        Unshared File {item.itemName} on {new Date(item.activityTime).toLocaleString()}
                    </div>
                )
            }
            else if(item.itemType==="d") {
                return(
                    <div>
                        Unshared Directory {item.itemName} on {new Date(item.activityTime).toLocaleString()}
                    </div>
                )
            }
        }
        else if(item.activityType==="delete"){
            if(item.itemType==="f")
            {
                return(
                    <div>
                        Deleted File {item.itemName} on {new Date(item.activityTime).toLocaleString()}
                    </div>
                )
            }
            else if(item.itemType==="d") {
                return(
                    <div>
                        Deleted Directory {item.itemName} on {new Date(item.activityTime).toLocaleString()}
                    </div>
                )
            }
        }
    });

    render(){

        const {item} = this.props;

        console.log(item);
        return(
            <tbody>
            <tr>

                <td className="text-justify">
                    {this.formatActivity(item)}
                    {/*{item.activitytype}*/}
                    {/*{item.activitytime}*/}
                    {/*{item.name}*/}
                </td>
                {/*{item.name && ( //Just a change here*/}
                    {/*<td className="text-justify">*/}
                        {/*{item.name}*/}
                    {/*</td>*/}
                {/*)}*/}
            </tr>
            </tbody>
        );
    }
}

export default ShowActivity;