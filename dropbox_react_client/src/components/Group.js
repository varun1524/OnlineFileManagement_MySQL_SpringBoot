import React, {Component} from 'react';
import * as API from '../api/API';
import {Route,Switch, withRouter} from 'react-router-dom';
import GroupDetails from "./GroupDetails";
import ShowGroups from "./ShowGroups";

class Group extends Component {

    // static propTypes = {
    //     message: PropTypes.string.isRequired,
    //     title: PropTypes.string.isRequired,
    //     visible: PropTypes.bool
    // };

    constructor(){
        super();
        this.state = {
            message : "",
            groups : [],
            selectedgroup : ""
        };

    }

    createGroup = (()=> {
        let grpName = prompt("Please enter group name:", "New Group");
        if (grpName === null || grpName === "") {
            console.log("User cancelled the prompt.");
        }
        else {
            if(grpName.trim()!=='') {
                console.log("Group Name: " + grpName);
                let data = {
                    groupName: grpName,
                    creationtime : new Date().toISOString().slice(0, 19).replace('T', ' ')
                };
                console.log(data);
                API.createGroup(data).then((response) => {
                    if(response.status===201){
                        this.fetchGroups();
                    }
                    else if(response.status === 203){
                        this.setState({
                            ...this.state,
                            message : "Session Expired"
                        });
                        this.props.handlePageChange("/");
                    }
                    else if(response.status === 301) {
                        response.json((data)=>{
                            console.log(data);
                            this.setState({
                                ...this.state,
                                message : data.message
                            });
                        });
                    }
                });
            }
            else {
                console.log("Group Name is Empty");
            }
        }
    });

    fetchGroups = (() => {
        API.getGroups().then((response) => {
            if (response.status === 204) {
                this.setState({
                    ...this.state,
                    groups: [],
                    message: "No Groups",
                });
            }
            else if(response.status === 201) {
                response.json().then((data) => {
                    this.setState({
                        ...this.state,
                        groups: data,
                        message: "Groups Received",
                    });
                });
            }
            else if (response.status === 301) {
                this.setState({
                    ...this.state,
                    message: "Error while loading Groups"
                });
            }
            else if (response.status === 203) {
                this.setState({
                    ...this.state,
                    message: "Session Expired."
                });
                this.props.handlePageChange("/");
            }
            else {
                console.log("Error");
            }
        });
    });

    handleDeleteGroup = ((group)=>{
        console.log(group);
        API.deleteGroup({groupid:group.groupid}).then((response)=>{
            console.log(response.status);
            if(response.status===201){
                response.json().then((data)=>{
                    this.setState({
                        ...this.state,
                        message : data+ "group removed"
                    });
                    this.fetchGroups();
                })
            }
            else if(response.status===203){
                console.log("Session Expired. Redirecting to Login");
                this.props.handlePageChange("/");
            }
            else if(response.status===301){
                response.json().then((data)=>{
                    this.setState({
                        ...this.state,
                        message : data.message
                    })
                })
            }
        });
    });

    componentWillMount(){
        API.getSession().then((response)=>{
            if(response.status===201){
                this.fetchGroups();
            }
            else if(response.status===203){
                this.props.handlePageChange("/");
            }
            else{
                console.log("Error");
            }
        });
    }

    componentDidMount(){
    }

    componentDidUpdate(){
    }

    componentWillUpdate(){
        /*API.getSession().then((response)=>{
            if(response.status===201){
            }
            else if(response.status===203){
                this.props.handlePageChange("/");
            }
            else{
                console.log("Error");
            }
        });*/
    }

    render() {
        return (
            <div className="container-fluid">
                <Switch>
                    <Route exact path="/user/group" render={(()=>{
                        return(
                            <div>
                                <div className="col-lg-9 col-xs-9 col-md-9 col-sm-9">
                                    <div className="row">
                                    </div>
                                    <div className="row">
                                        <div className="row">
                                        </div>
                                        <div className="row" id="example">
                                            <div className="table table-responsive ">
                                                <table className="table table-responsive text-justify ">
                                                    <thead>
                                                    <tr>
                                                        <th>Group Name</th>
                                                        <th>Creation Time</th>
                                                        <th>GroupOwner</th>
                                                        <th>UserAccess</th>
                                                    </tr>
                                                    </thead>
                                                    {
                                                        // showGroups
                                                        this.state.groups.map((item, index) => {
                                                            return(<ShowGroups
                                                                key={index}
                                                                item={item}
                                                                handleDeleteGroup = {this.handleDeleteGroup}
                                                            />)
                                                        })
                                                    }
                                                </table>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div className="col-lg-2 col-xs-2 col-md-2 col-sm-2 right">
                                    <div className="container-fluid right">
                                        <div className="row">
                                            {this.state.message && ( //Just a change here
                                                <div className="alert alert-info" >
                                                    {this.state.message}
                                                </div>
                                            )}
                                        </div>
                                        <div className="row">
                                            <button className="btn btn-primary" value="Create Group" onClick={()=>this.createGroup()}>
                                                Create Group
                                            </button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        )
                    })}/>
                    {/*<Route path="/user/group/:groupid" component={GroupDetails} data={this.state.groups}/>*/}
                    <Route path="/user/group/:groupid" render={((match)=>{
                        return(
                            <GroupDetails
                                groups={this.state.groups}
                                context={match}
                            />
                        )
                    })}/>
                </Switch>
            </div>
        );
    }
}

export default withRouter(Group);