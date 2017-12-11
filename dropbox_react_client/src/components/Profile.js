import React, {Component} from 'react';
import * as API from '../api/API';


class Profile extends Component{

    constructor(){
        super();
        this.state = {
            lifeevent:"",
            mobile :"",
            overview:"",
            work:"",
            reading:false,
            music:false,
            sports:false,
            username:"",
            education:""
        };
    }

    handleSubmitProfileChange = (()=> {
        API.changeProfile(this.userData).then((response) => {
            if(response.status===201){
                console.log("Added successfully");
                this.props.handlePageChange("/user/profile");
            }
            else  if(response.status===203){
                this.props.handlePageChange("/home/login");
            }
            else  if(response.status===301){
                console.log("Error while adding profile data")
            }
        });
    });

    componentWillMount(){
        API.getprofile().then((response)=>{
            console.log(response.status);
            if(response.status===201){
                response.json().then((data)=>{
                    console.log(data);
                    this.setState({
                        lifeevent:data.lifeevent,
                        mobile :data.mobile,
                        overview:data.overview,
                        work:data.work,
                        reading:data.reading,
                        music:data.music,
                        sports:data.sports,
                        username:data.username,
                        education:data.education
                    });
                    this.userData = data;
                });
            }
            else {
                console.log("Error");
            }
        });
    }

    render(){
        return(
            <div className="container-fluid">
                <div>
                    <form className="form-horizontal">
                        <div className="form-group">
                            <label className="text-justify h3">Edit Profile</label><hr/>
                        </div>
                        <div className="form-group">
                            <div className="col-sm-8 col-md-8 col-lg-8 col-xs-offset-2 col-sm-offset-2 col-md-offset-2 col-lg-offset-2">


                                <div className="col-sm-3 col-md-3 col-lg-3">
                                    <label className="form-horizontal form-control-static">Username:</label>
                                </div>
                                <div className="col-sm-5 col-md-5 col-lg-5 ">
                                    <input
                                        type="text"
                                        className="form-control"
                                        id="txtoverview"
                                        value={this.state.username}
                                        disabled
                                    />
                                </div>
                            </div>
                        </div>
                        <div className="form-group">
                            <div className="col-sm-8 col-md-8 col-lg-8 col-xs-offset-2 col-sm-offset-2 col-md-offset-2 col-lg-offset-2">
                                <div className="col-sm-3 col-md-3 col-lg-3">
                                    <label className="form-horizontal form-control-static">Overview:</label>
                                </div>
                                <div className="col-sm-5 col-md-5 col-lg-5 ">
                                    <input
                                        type="text"
                                        className="form-control"
                                        id="txtoverview"
                                        value={this.state.overview}
                                        required
                                        onChange={(event) => {
                                            this.setState({
                                                // ...this.state,
                                                overview: event.target.value
                                            });
                                            this.userData.overview=event.target.value;
                                        }}
                                    />
                                </div>
                            </div>
                        </div>
                        <div className="form-group">
                            <div className="col-sm-8 col-md-8 col-lg-8 col-xs-offset-2 col-sm-offset-2 col-md-offset-2 col-lg-offset-2">
                                <div className="col-sm-3 col-md-3 col-lg-3">
                                    <label className="form-horizontal form-control-static">Work:</label>
                                </div>
                                <div className="col-sm-5 col-md-5 col-lg-5">
                                    <input
                                        type="text"
                                        className="form-control"
                                        id="txtwork"
                                        value={this.state.work}
                                        onChange={(event) => {
                                            this.setState({
                                                // ...this.state,
                                                work: event.target.value
                                            });
                                            this.userData.work = event.target.value;
                                        }}
                                    />
                                </div>
                            </div>
                        </div>
                        <div className="form-group">
                            <div className="col-sm-8 col-md-8 col-lg-8 col-xs-offset-2 col-sm-offset-2 col-md-offset-2 col-lg-offset-2">
                                <div className="col-sm-3 col-md-3 col-lg-3">
                                    <label className="form-horizontal form-control-static">Education:</label>
                                </div>
                                <div className="col-sm-5 col-md-5 col-lg-5">
                                    <input
                                        type="text"
                                        className="form-control"
                                        id="txteducation"
                                        value={this.state.education}
                                        onChange={(event) => {
                                            this.setState({
                                                // ...this.state,
                                                education: event.target.value
                                            });
                                            this.userData.education = event.target.value;
                                        }}
                                    />
                                </div>
                            </div>
                        </div>
                        <div className="form-group">
                            <div className="col-sm-8 col-md-8 col-lg-8 col-xs-offset-2 col-sm-offset-2 col-md-offset-2 col-lg-offset-2">
                                <div className="col-sm-3 col-md-3 col-lg-3">
                                    <label className="form-horizontal form-control-static">Contact:</label>
                                </div>
                                <div className="col-sm-5 col-md-5 col-lg-5">
                                    <input
                                        type="text"
                                        className="form-control"
                                        id="txtcontact"
                                        value={this.state.mobile}
                                        onChange={(event) => {
                                            this.setState({
                                                // ...this.state,
                                                mobile: event.target.value
                                            });
                                            this.userData.mobile = event.target.value;
                                        }}
                                    />
                                </div>
                            </div>
                        </div>
                        <div className="form-group">
                            <div className="col-sm-8 col-md-8 col-lg-8 col-xs-offset-2 col-sm-offset-2 col-md-offset-2 col-lg-offset-2">
                                <div className="col-sm-3 col-md-3 col-lg-3">
                                    <label className="form-horizontal form-control-static">Life Events:</label>
                                </div>
                                <div className="col-sm-5 col-md-5 col-lg-5">
                                    <input
                                        type="text"
                                        className="form-control"
                                        id="txtlifeevents"
                                        value={this.state.lifeevent}
                                        onChange={(event) => {
                                            this.setState({
                                                // ...this.state,
                                                lifeevent: event.target.value
                                            });
                                            this.userData.lifeevent = event.target.value;
                                        }}
                                    />
                                </div>
                            </div>
                        </div>
                        <div className="form-group">
                            <div className="col-sm-8 col-md-8 col-lg-8 col-xs-offset-2 col-sm-offset-2 col-md-offset-2 col-lg-offset-2">
                                <div className="col-sm-3 col-md-3 col-lg-3">
                                    <label className="form-horizontal form-control-static">Interests:</label>
                                </div>
                                <div className="col-sm-5 col-md-5 col-lg-5">
                                    <div className="row checkbox">
                                        <input
                                            type="checkbox"
                                            id="cbmusic"
                                            value={this.state.music}
                                            onChange={(event) => {
                                                this.setState({
                                                    // ...this.state,
                                                    music: event.target.checked
                                                });
                                                this.userData.music = event.target.checked;
                                            }}
                                        />Music
                                    </div>

                                    <div className="row checkbox">
                                        <input
                                            type="checkbox"
                                            id="cbreading"
                                            value={this.state.reading}
                                            onChange={(event) => {
                                                this.setState({
                                                    // ...this.state,
                                                    reading: event.target.checked
                                                });
                                                this.userData.reading = event.target.checked;
                                            }}
                                        />Reading
                                    </div>

                                    <div className="row checkbox">
                                        <input
                                            type="checkbox"
                                            id="cbsports"
                                            value={this.state.sports}
                                            onChange={(event) => {
                                                this.setState({
                                                    // ...this.state,
                                                    sports: event.target.checked
                                                });
                                                this.userData.sports = event.target.checked;
                                            }}
                                        />Sports
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div className="form-group">
                            <input type="button" className="btn btn-primary" value="Save" onClick={(()=>{this.handleSubmitProfileChange()})}/>
                        </div>
                    </form>
                </div>



                {/*<label>Interest</label><hr/>*/}
                {/*<div className="form-group">*/}
                {/*<input type="checkbox" name="interest" value="Music" />Music*/}
                {/*<input type="checkbox" value="Sports" name="interest"/>Sports*/}
                {/*<input type="checkbox" value="Reading" name="interest"/>Reading*/}
                {/*</div><br/>*/}
            </div>
        );
    }
}


export default Profile;