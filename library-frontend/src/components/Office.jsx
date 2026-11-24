import React from 'react'
import LibraryButton from './LibraryButton'
import { useState } from 'react'
import axios from 'axios'
import LibraryHeader from './LibraryHeader'

function Office() {
    const [name, setName] = useState("");
    const [officeNumber, setOfficeNumber] = useState("");

    const [ofNumber, setOfNumber] = useState([]);


    async function saveOffice() {
        const response = await axios.post("http://localhost:8082/api/office/save", {
            name: name,
            officeNumber: officeNumber
        })
        if (response.status == 201){
            alert("Office saved successfully");
        } else {
            alert("Office not saved");
        };
    }
    async function deleteOffice() {
        const response = await axios.delete(`http://localhost:8082/api/office/number/${ofNumber}`);
        if (response.status == 200){
            alert("Office deleted successfully");
        } else {
            alert("Office not deleted");
        }
    }
  return (
    <div>
      <LibraryHeader />
      <h1>SAVE OFFICE</h1>
      <label>Oficee Name:<input type="text"  onChange={(e) => setName(e.target.value)}/></label><br/>
      <label>Office Number:<input type="text"  onChange={(e) => setOfficeNumber(e.target.value)}/></label><br/>
      <LibraryButton buttonLabel= "SAVE" onClickButton={saveOffice}/>
      <h2> Delete Office</h2>
      <input type='text' onChange={(e) => setOfNumber(e.target.value)}/>
      <LibraryButton buttonLabel ="DELETE" onClickButton={deleteOffice}/>
    </div>
  )
}

export default Office
