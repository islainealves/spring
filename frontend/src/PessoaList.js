import React, { Component } from 'react';
import { Button, ButtonGroup, Container, Table } from 'reactstrap';
import AppNavbar from './AppNavbar';
import { Link } from 'react-router-dom';

class PessoaList extends Component {

    constructor(props) {
        super(props);
        this.state = {pessoas: []};
        this.remove = this.remove.bind(this);
    }

    componentDidMount() {
        fetch('/pessoa')
            .then(response => response.json())
            .then(data => this.setState({pessoas: data}));
    }

    async remove(id) {
        await fetch(`/pessoa/${id}`, {
            method: 'DELETE',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            }
        }).then(() => {
            let updatedPessoas = [...this.state.pessoas].filter(i => i.id !== id);
            this.setState({pessoas: updatedPessoas});
        });
    }

    render() {
        const {pessoas} = this.state;

        const pessoaList = pessoas.map(pessoa => {
            return <tr key={pessoa.id}>
                <td style={{whiteSpace: 'nowrap'}}>{pessoa.name}</td>
                <td>
                    <ButtonGroup>
                        <Button size="sm" color="primary" tag={Link} to={"/pessoa/" + pessoa.id}>Edit</Button>
                        <Button size="sm" color="danger" onClick={() => this.remove(pessoa.id)}>Delete</Button>
                    </ButtonGroup>
                </td>
            </tr>
        });

        return (
            <div>
                <AppNavbar/>
                <Container fluid>
                    <div className="float-right">
                        <Button color="success" tag={Link} to="/pessoa/new">Add Pessoa</Button>
                    </div>
                    <h3>Pessoas</h3>
                    <Table className="mt-4">
                        <thead>
                        <tr>
                            <th width="30%">Nome</th>
                            <th width="40%">Actions</th>
                        </tr>
                        </thead>
                        <tbody>
                        {pessoaList}
                        </tbody>
                    </Table>
                </Container>
            </div>
        );
    }
}

export default PessoaList;