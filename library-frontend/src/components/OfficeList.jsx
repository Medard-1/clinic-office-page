import React, { useEffect, useState } from 'react'
import axios from 'axios'
import LibraryButton from './LibraryButton'

function OfficeList() {
    const [offices, setOffices] = useState([]);
    const [keyword, setKeyword] = useState("");

    async function loadOffices() {
        const response = await axios.get("http://localhost:8082/api/office/all");
        setOffices(response.data);
    }

    async function searchOffices() {
        if (keyword === "") {
            loadOffices();
        } else {
            const response = await axios.get("http://localhost:8082/api/office/search", {
                params: { keyword: keyword }
            });
            setOffices(response.data);
        }
    }

    async function deleteOffice(officeNumber) {
        try {
            const response = await axios.delete(`http://localhost:8082/api/office/number/${officeNumber}`);
            if (response.status == 200) {
                alert("Office deleted successfully");
                loadOffices();
            }
        } catch (error) {
            alert("Office not deleted");
        }
    }

    function updateOffice(office) {
        // update code goes here
    }

    // load all offices when the page opens
    useEffect(() => {
        loadOffices();
    }, []);

    return (
        <div>
            <h1>OFFICE LIST</h1>

            <input type="text" placeholder="Search by office name or number"
                onChange={(e) => setKeyword(e.target.value)} />
            <LibraryButton buttonLabel="SEARCH" onClickButton={searchOffices} />

            <table border="1">
                <thead>
                    <tr>
                        <th>No</th>
                        <th>Office Name</th>
                        <th>Office Number</th>
                        <th>Action</th>
                    </tr>
                </thead>
                <tbody>
                    {offices.map((office, index) => (
                        <tr key={office.id}>
                            <td>{index + 1}</td>
                            <td>{office.name}</td>
                            <td>{office.officeNumber}</td>
                            <td>
                                <LibraryButton buttonLabel="UPDATE" onClickButton={() => updateOffice(office)} />
                                <LibraryButton buttonLabel="DELETE" onClickButton={() => deleteOffice(office.officeNumber)} />
                            </td>
                        </tr>
                    ))}
                </tbody>
            </table>
        </div>
    )
}

export default OfficeList
